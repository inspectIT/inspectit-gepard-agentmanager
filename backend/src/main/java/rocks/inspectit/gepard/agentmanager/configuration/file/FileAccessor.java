/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileAccessor {

  private final Path filePath;

  private final Lock readLock;

  private final Lock writeLock;

  private FileAccessor(Path filePath, ReadWriteLock readWriteLock) {
    this.filePath = filePath;
    this.readLock = readWriteLock.readLock();
    this.writeLock = readWriteLock.writeLock();
  }

  /**
   * Factory method to create a {@link FileAccessor}
   *
   * @param filePath the path of the accessible file
   * @return the created accessor
   */
  public static FileAccessor create(Path filePath) {
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    return new FileAccessor(filePath, readWriteLock);
  }

  /**
   * Tries to read data from a file.
   *
   * @return the content of the file
   */
  public String readFile() throws IOException {
    readLock.lock();
    try {
      if (Files.notExists(filePath)) throw new FileNotFoundException("File not found: " + filePath);

      if (!Files.isReadable(filePath))
        throw new AccessDeniedException("File is not readable: " + filePath);

      byte[] rawFileContent = Files.readAllBytes(filePath);
      return new String(rawFileContent);
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Tries to write data into a file.
   *
   * @param content the data, which should be written into the file
   */
  public void writeFile(String content) throws IOException {
    writeLock.lock();
    try {
      if (Files.notExists(filePath)) {
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);
      }

      if (!Files.isWritable(filePath))
        throw new AccessDeniedException("File is not writable: " + filePath);

      Files.write(filePath, content.getBytes());
    } finally {
      writeLock.unlock();
    }
  }
}
