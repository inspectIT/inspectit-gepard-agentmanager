/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
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
import org.springframework.test.util.ReflectionTestUtils;
import rocks.inspectit.gepard.agentmanager.exception.GitOperationException;

@ExtendWith(MockitoExtension.class)
class GitServiceTest {

  @Mock private Git git;

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
        assertThrows(
            GitOperationException.class,
            () -> {
              gitService.commit();
            });

    assertTrue(exception.getMessage().contains("Failed to commit changes"));
  }

  @Test
  void testUpdateFileContentSuccess() throws IOException {
    String content = "{\"key\": \"value\"}";
    File testFile = new File(tempRepoPath, "configuration.json");

    gitService.updateFileContent(content);

    assertTrue(testFile.exists());
    String fileContent = Files.readString(testFile.toPath());
    assertEquals(content, fileContent);
  }

  @Test
  void testUpdateFileContentFailure() {
    ReflectionTestUtils.setField(gitService, "localRepoPath", "/invalid-path");

    GitOperationException exception =
        assertThrows(
            GitOperationException.class,
            () -> {
              gitService.updateFileContent("content");
            });

    assertTrue(exception.getMessage().contains("Failed to update file content"));
  }

  @Test
  void testGetFileContentSuccess() throws IOException {
    String content = "{\"key\": \"value\"}";
    Path filePath = Path.of(tempRepoPath, "configuration.json");
    Files.writeString(filePath, content);

    String result = gitService.getFileContent();

    assertEquals(content, result);
  }

  @Test
  void testGetFileContentFileNotFound() {
    GitOperationException exception =
        assertThrows(
            GitOperationException.class,
            () -> {
              gitService.getFileContent();
            });

    assertTrue(exception.getMessage().contains("Failed to read file content"));
  }

  @Test
  void testInitializeLocalRepositoryWhenRepoDoesNotExist() throws GitAPIException, IOException {
    Files.delete(Path.of(tempRepoPath));

    InitCommand initCommand = mock(InitCommand.class);
    try (MockedStatic<Git> mockedGit = Mockito.mockStatic(Git.class)) {
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

      mockedGit.verify(() -> Git.open(new File(tempRepoPath)), times(1));
      mockedGit.verify(() -> Git.init(), times(0));
    }
  }

  @Test
  void testInitializeLocalRepositoryWhenRepoDoesNotExistWithGitAPIException()
      throws IOException, GitAPIException {
    Files.delete(Path.of(tempRepoPath));

    InitCommand initCommand = mock(InitCommand.class);
    try (MockedStatic<Git> mockedGit = Mockito.mockStatic(Git.class)) {
      mockedGit.when(Git::init).thenReturn(initCommand);
      when(initCommand.setDirectory(any())).thenReturn(initCommand);
      when(initCommand.call()).thenThrow(new GitAPIException("Git error") {});

      GitOperationException exception =
          assertThrows(
              GitOperationException.class,
              () -> {
                gitService.initializeLocalRepository();
              });

      assertTrue(exception.getMessage().contains("Failed to initialize local repository"));
    }
  }

  @Test
  void testInitializeLocalRepositoryWhenRepoAlreadyExistsWithIOException() {
    assertTrue(Files.exists(Path.of(tempRepoPath)));

    try (MockedStatic<Git> mockedGit = Mockito.mockStatic(Git.class)) {
      mockedGit.when(() -> Git.open(any(File.class))).thenThrow(IOException.class);

      GitOperationException exception =
          assertThrows(
              GitOperationException.class,
              () -> {
                gitService.initializeLocalRepository();
              });

      assertTrue(exception.getMessage().contains("Failed to open local repository"));
    }
  }
}
