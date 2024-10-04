/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import rocks.inspectit.gepard.agentmanager.configuration.file.FileAccessor;
import rocks.inspectit.gepard.agentmanager.exception.FileAccessException;
import rocks.inspectit.gepard.agentmanager.exception.GitOperationException;

@ExtendWith(MockitoExtension.class)
class GitServiceTest {

  @Mock private Git git;

  @Mock private FileAccessor fileAccessor;

  @InjectMocks private GitService gitService;

  private String tempRepoPath;

  @BeforeEach
  public void setUp() throws Exception {
    Path tempDirectory = Files.createTempDirectory("test-repo");
    tempRepoPath = tempDirectory.toString();

    ReflectionTestUtils.setField(gitService, "localRepoPath", tempRepoPath);
  }

  @Test
  void testCommitSuccess() {
    AddCommand addCommand = mock(AddCommand.class);
    CommitCommand commitCommand = mock(CommitCommand.class);

    when(git.add()).thenReturn(addCommand);
    when(addCommand.addFilepattern(anyString())).thenReturn(addCommand);
    when(git.commit()).thenReturn(commitCommand);
    when(commitCommand.setMessage(anyString())).thenReturn(commitCommand);

    gitService.commit();

    verify(git).add();
    verify(git).commit();
  }

  @Test
  void testCommitFailure() throws GitAPIException {
    when(git.add()).thenReturn(mock(AddCommand.class));
    when(git.add().addFilepattern(anyString())).thenReturn(mock(AddCommand.class));
    when(git.commit()).thenReturn(mock(CommitCommand.class));
    when(git.commit().setMessage(anyString())).thenReturn(mock(CommitCommand.class));
    when(git.commit().setMessage(anyString()).call())
        .thenThrow(new GitAPIException("Git error") {});

    GitOperationException exception =
        assertThrows(GitOperationException.class, () -> gitService.commit());

    assertTrue(exception.getMessage().contains("Failed to commit changes"));
  }

  @Test
  void testUpdateFileContentSuccess() throws IOException {
    gitService.updateFileContent("content");

    verify(fileAccessor, times(1)).writeFile("content");
  }

  @Test
  void testUpdateFileContentFailure() throws IOException {
    doThrow(AccessDeniedException.class).when(fileAccessor).writeFile("content");

    FileAccessException exception =
        assertThrows(FileAccessException.class, () -> gitService.updateFileContent("content"));

    assertTrue(exception.getMessage().contains("Failed to update file content"));
    assertInstanceOf(AccessDeniedException.class, exception.getCause());
  }

  @Test
  void testGetFileContentSuccess() throws IOException {
    String content = "{\"key\": \"value\"}";

    when(fileAccessor.readFile()).thenReturn(content);

    String result = gitService.getFileContent();

    assertEquals(content, result);
  }

  @Test
  void testGetFileContentFileNotFound() throws IOException {
    FileAccessException fileAccessException =
        new FileAccessException(
            "File not found", new FileNotFoundException(), HttpStatus.NOT_FOUND);
    when(fileAccessor.readFile()).thenThrow(fileAccessException);
    FileAccessException exception =
        assertThrows(FileAccessException.class, () -> gitService.getFileContent());

    assertTrue(exception.getMessage().contains("File not found"));
    assertInstanceOf(FileNotFoundException.class, exception.getCause());
  }

  @Test
  void testGetFileContentFileFailedToRead() throws IOException {
    FileAccessException fileAccessException =
        new FileAccessException(
            "File failed to read", new IOException(), HttpStatus.INTERNAL_SERVER_ERROR);
    when(fileAccessor.readFile()).thenThrow(fileAccessException);
    FileAccessException exception =
        assertThrows(FileAccessException.class, () -> gitService.getFileContent());

    assertTrue(exception.getMessage().contains("File failed to read"));
    assertInstanceOf(IOException.class, exception.getCause());
  }

  @Test
  void testInitializeLocalRepositoryWhenRepoDoesNotExist() throws GitAPIException, IOException {
    Files.delete(Path.of(tempRepoPath));

    InitCommand initCommand = mock(InitCommand.class);
    try (MockedStatic<Git> mockedGit = Mockito.mockStatic(Git.class)) {
      mockedGit.when(() -> Git.open(any(File.class))).thenThrow(IOException.class);
      mockedGit.when(Git::init).thenReturn(initCommand);
      when(initCommand.setDirectory(any())).thenReturn(initCommand);
      when(initCommand.call()).thenReturn(mock(Git.class));

      gitService.initializeLocalRepository();

      verify(initCommand, times(1)).setDirectory(any());
    }
  }

  @Test
  void testInitializeLocalRepositoryWhenRepoAlreadyExists() {
    assertTrue(Files.exists(Path.of(tempRepoPath)));

    try (MockedStatic<Git> mockedGit = Mockito.mockStatic(Git.class)) {
      mockedGit.when(() -> Git.open(any(File.class))).thenReturn(git);

      gitService.initializeLocalRepository();

      mockedGit.verify(() -> Git.open(new File(tempRepoPath)), times(2));
      mockedGit.verify(() -> Git.init(), times(0));
    }
  }

  @Test
  void testInitializeLocalRepositoryWhenRepoDoesNotExistWithGitAPIException()
      throws IOException, GitAPIException {
    Files.delete(Path.of(tempRepoPath));

    InitCommand initCommand = mock(InitCommand.class);
    try (MockedStatic<Git> mockedGit = Mockito.mockStatic(Git.class)) {
      mockedGit.when(() -> Git.open(any(File.class))).thenThrow(IOException.class);
      mockedGit.when(Git::init).thenReturn(initCommand);
      when(initCommand.setDirectory(any())).thenReturn(initCommand);
      when(initCommand.call()).thenThrow(new GitAPIException("Git error") {});

      GitOperationException exception =
          assertThrows(GitOperationException.class, () -> gitService.initializeLocalRepository());

      assertTrue(exception.getMessage().contains("Failed to initialize local repository"));
    }
  }

  @Test
  void testInitializeLocalRepositoryWhenRepoAlreadyExistsWithIOException() {
    assertTrue(Files.exists(Path.of(tempRepoPath)));
    ;

    try (MockedStatic<Git> mockedGit = Mockito.mockStatic(Git.class)) {
      mockedGit
          .when(() -> Git.open(any(File.class)))
          .thenCallRealMethod()
          .thenThrow(IOException.class);

      GitOperationException exception =
          assertThrows(GitOperationException.class, () -> gitService.initializeLocalRepository());

      assertTrue(exception.getMessage().contains("Failed to open local repository"));
    }
  }
}
