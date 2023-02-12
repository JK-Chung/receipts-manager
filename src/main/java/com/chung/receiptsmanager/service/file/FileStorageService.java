package com.chung.receiptsmanager.service.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface FileStorageService {
    Optional<Path> getFile(final String fileLocation) throws IOException;
    String storeFile(final Path sourceFile) throws IOException;
    void deleteFile(final String fileLocation) throws IOException;
}
