/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.configuration.file.FileAccessor;
import rocks.inspectit.gepard.agentmanager.exception.FileAccessException;
import rocks.inspectit.gepard.agentmanager.exception.GitOperationException;

/** Service-Implementation for communication with Git repositories. */
@Slf4j
@Service
public class GitService {

  private static final String FILE = "configuration.json";

  @Value("${inspectit-config-server.configurations.local-path}")
  private String localRepoPath;

  private FileAccessor fileAccessor;

  private Git git;

  @PostConstruct
  public void init() {
    fileAccessor = FileAccessor.create(Path.of(localRepoPath + "/" + FILE));
  }

  /**
   * Add and commit the current changes to the local Git repository.
   *
   * @throws GitOperationException if commiting the changes fails
   */
  public void commit() {
    try {
      git.add().addFilepattern(FILE).call();
      git.commit().setMessage("update configuration").call();
    } catch (GitAPIException e) {
      throw new GitOperationException("Failed to commit changes to local repository", e);
    }
  }

  /**
   * Update the content of the current configuration file.
   *
   * @param content the content to be saved a {@link String}
   * @throws FileAccessException if updating the file fails
   */
  public void updateFileContent(String content) {
    try {
      fileAccessor.writeFile(content);
    } catch (IOException e) {
      throw new FileAccessException("Failed to update file content", e);
    }
  }

  /**
   * Retrieve the current content of the configuration file.
   *
   * @return the current configuration as {@link String}
   * @throws FileAccessException if reading the file fails
   */
  public String getFileContent() {
    try {
      return fileAccessor.readFile();
    } catch (IOException e) {
      throw new FileAccessException("Failed to read file content", e);
    }
  }

  /**
   * Initialize the local Git repository. If there is no local repository found, a new one is
   * created. Otherwise, the existing one is used.
   *
   * @throws GitOperationException if the initialization of the local repository fails or if it
   *     fails to open the local repository
   */
  public void initializeLocalRepository() {
    File file = Path.of(localRepoPath).toFile();

    try {
      if (!isGitRepository(file)) {
        log.info("Local repository does not exist, creating local repository");
        git = Git.init().setDirectory(file).call();
      } else {
        log.info("Local repository found, using local repository");
        git = Git.open(file);
      }
    } catch (GitAPIException gitAPIException) {
      throw new GitOperationException("Failed to initialize local repository", gitAPIException);
    } catch (IOException ioException) {
      throw new GitOperationException("Failed to open local repository", ioException);
    }
  }

  private boolean isGitRepository(File file) {
    try (Git ignored = Git.open(file)) {
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
