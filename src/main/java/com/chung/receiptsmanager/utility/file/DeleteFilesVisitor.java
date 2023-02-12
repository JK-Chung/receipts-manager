package com.chung.receiptsmanager.utility.file;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class DeleteFilesVisitor extends SimpleFileVisitor<Path> {
    
    @Override
    public FileVisitResult visitFile(Path pathOfCurrFile, BasicFileAttributes attributes)
            throws IOException {
        try {
            Files.delete(pathOfCurrFile);
            log.info("Successfully deleted file at " + pathOfCurrFile);
            return FileVisitResult.CONTINUE;
        } catch(Exception ex) {
            log.warn("Exception ({}) when deleting file at path {}", ex.getMessage(), pathOfCurrFile);
            throw ex;
        }
    }

    @Override
    public FileVisitResult postVisitDirectory(Path pathOfDirectory, IOException ex) throws IOException {
        if(ex != null) {
            log.warn("Experienced exception ({}) when trying to delete directory ({}) and its files",
                    ex.getMessage(), pathOfDirectory);
            throw ex;
        }

        try {
            Files.delete(pathOfDirectory);
            log.info("Successfully deleted directory (path = {}) and all files inside it", pathOfDirectory);
        } catch (IOException exception) {
            log.warn("Managed to delete all files inside directory (path = {}) but was unable to delete the " +
                    "directory itself due to exception", pathOfDirectory, exception);
        }
        return FileVisitResult.CONTINUE;
    }
    
}
