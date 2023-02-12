package com.chung.receiptsmanager.service.file.helper;

import com.chung.receiptsmanager.service.file.FileStorageService;
import com.chung.receiptsmanager.utility.file.DeleteFilesVisitor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class VolatileLocalFileStorage implements FileStorageService {

    private static final String STORAGE_DIRECTORY_NAME_PREFIX = "receipts-manager-volatile-";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

    private final Path storageDirectory;

    public VolatileLocalFileStorage(final String forFilename) throws IOException {
        this.storageDirectory = Files.createTempDirectory(STORAGE_DIRECTORY_NAME_PREFIX + forFilename + "-");
        log.info("Storage directory created at " + this.storageDirectory.toString());
    }

    public Path tempStoreFileOnServer(final InputStream srcInputStream) throws IOException {
        final String fileLocation = DATE_FORMAT.format(new Date()) + "--" + UUID.randomUUID();
        final Path destFilePath = generatePathToStoreNewFile(fileLocation);

        Files.copy(srcInputStream, destFilePath);
        return destFilePath;
    }

    @Override
    public String storeFile(final Path sourceFilePath) throws IOException {
        // generate our own custom filename (for security, don't trust user-provided filenames)
        final String fileLocation = DATE_FORMAT.format(new Date()) + "--" + UUID.randomUUID();
        final Path destFilePath = generatePathToStoreNewFile(fileLocation);

        try {
            log.info("Saving file (file-location = {}) to path {}...", fileLocation, destFilePath);
            Files.copy(sourceFilePath, destFilePath);
            log.info("Successfully saved file (file-location = {}) to path {}...", fileLocation, destFilePath);
            return fileLocation;
        } catch (FileAlreadyExistsException ex) {
            log.error("Was unable to save new file (file-location = {}) because a file already exists at {}",
                    fileLocation, destFilePath);
            throw ex;
        }
    }

    @Override
    public Optional<Path> getFile(String fileLocation) throws IOException {
        final Path path = generatePathToStoreNewFile(fileLocation);

        if(!Files.exists(path)) {
            log.debug("No file exists (file-location = {}, path = {})", fileLocation, path);
            return Optional.empty();
        }

        if(!Files.isRegularFile(path)) {
            log.warn("File (file-location = {}, path = {}) is somehow a directory? " +
                    "Application will act as if nothing exists at this path.", fileLocation, path);
            return Optional.empty();
        }

        log.debug("Successfully located file (file-location = {}, path = {})", fileLocation, path);
        return Optional.of(path);
    }

    @Override
    public void deleteFile(String fileLocation) throws IOException {
        Files.delete(generatePathToStoreNewFile(fileLocation));
    }



    private Path generatePathToStoreNewFile(final String fileLocation) {
        return Paths.get(this.storageDirectory.toString(), fileLocation);
    }

    @PreDestroy
    private void deleteStorageDirectory() {
        try {
            log.info("Cleaning up files stored by {} in directory {}...", this.getClass().getSimpleName(), this.storageDirectory.toString());
            Files.walkFileTree(this.storageDirectory, new DeleteFilesVisitor());
        } catch (IOException ex) {
            log.error("Could not delete all files in directory (%s) due to exception"
                    .formatted(this.storageDirectory.toUri().toString()), ex);
        }
    }
    
}
