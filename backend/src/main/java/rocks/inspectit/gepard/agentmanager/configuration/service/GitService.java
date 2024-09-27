/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.exception.GitOperationException;

@Slf4j
@Service
public class GitService {

  private static final String FILE = "configuration.json";

  @Value("${inspectit-config-server.configurations.local-path}")
  private String localRepoPath;

  private Git git;

  public void commit() {
    try {
      git.add().addFilepattern(FILE).call();
      git.commit().setMessage("update configuration").call();
    } catch (GitAPIException e) {
      throw new GitOperationException("Failed to commit changes to local repository", e);
    }
  }

  public void updateFileContent(String content) {
    try {
      File file = new File(localRepoPath + "/configuration.json");
      java.nio.file.Files.write(file.toPath(), content.getBytes());
    } catch (IOException e) {
      throw new GitOperationException("Failed to update file content", e);
    }
  }

  public String getFileContent() {
    try {
      Path filePath = Path.of(localRepoPath, FILE);
      if (Files.exists(filePath)) {
        return Files.readString(filePath);
      } else {
        throw new IOException("File not found: " + FILE);
      }
    } catch (IOException e) {
      throw new GitOperationException("Failed to read file content", e);
    }
  }

  public void initializeLocalRepository() {
    Path path = Path.of(localRepoPath);

    try {
      if (!Files.exists(path)) {
        log.warn("Local repository does not exist, creating local repository");
        git = Git.init().setDirectory(path.toFile()).call();
      } else {
        log.info("Local repository found, using local repository");
        git = Git.open(path.toFile());
      }
    } catch (GitAPIException gitAPIException) {
      throw new GitOperationException("Failed to initialize local repository", gitAPIException);
    } catch (IOException ioException) {
      throw new GitOperationException("Failed to open local repository", ioException);
    }
  }
}
