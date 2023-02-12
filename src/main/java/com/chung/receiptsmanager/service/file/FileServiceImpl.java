package com.chung.receiptsmanager.service.file;

import com.chung.receiptsmanager.entity.FileEntity;
import com.chung.receiptsmanager.entity.UserEntity;
import com.chung.receiptsmanager.exceptions.responseStatusException.file.ActualFileTypeDoesNotMatchFileExtensionException;
import com.chung.receiptsmanager.exceptions.responseStatusException.file.UnacceptableFileTypeException;
import com.chung.receiptsmanager.repository.file.FileRepository;
import com.chung.receiptsmanager.service.security.CurrentAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final FileValidationService fileValidationService;
    private final CurrentAuthenticationService currentAuthenticationService;

    public FileServiceImpl(
            FileRepository fileRepository,
            FileStorageService fileStorageService,
            FileValidationService fileValidationService,
            CurrentAuthenticationService currentAuthenticationService
    ) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
        this.fileValidationService = fileValidationService;
        this.currentAuthenticationService = currentAuthenticationService;
    }

    @Override
    public UUID saveFileAndMetadata(final Path toSave) throws IOException {
        final String fileLocation = fileStorageService.storeFile(toSave);
        this.fileValidationService.validateFile(toSave, () -> cleanupStoredFile(fileLocation)); // TODO consider validating file before storing it

        final UUID id = UUID.randomUUID();
        log.info("File (id={}, file-location={}) was successfully stored and validated. Now persiting metadata...",
                id, fileLocation);
        persistFileMetadata(toSave, id, fileLocation, () -> cleanupStoredFile(fileLocation));
        return id;
    }

    private void persistFileMetadata(
            final Path file,
            final UUID id,
            final String fileLocation,
            final Runnable cleanupOnFailure
    ) {
        try {
            persistFileMetadata(file, id, fileLocation);
        } catch (Exception ex) {
            log.error("Exception when trying to persist metadata on saved file (id={}, file-location={}). " +
                    "Will run cleanup function {}.", id, fileLocation, cleanupOnFailure.toString(), ex);
            cleanupOnFailure.run();
            throw ex;
        }
    }

    private void persistFileMetadata(final Path file, final UUID id, final String fileLocation) {
        final UserEntity owner = currentAuthenticationService.getCurrentlyAuthenticatedUserEntity();
        log.info("User (id={}, username={}) will be identified as the owner of the file (id={}, file-location={})",
                owner.getId(), owner.getUsername(), id, fileLocation);

        final var entityToSave = FileEntity.builder()
                .id(id)
                .owner(owner)
                .userProvidedFileName(file.getFileName().toString()) // TODO sanitise
                .fileExtension(FileEntity.FileExtension.PDF) // TODO extract file extension
                .fileLocation(fileLocation)
                .build();

        log.info("Persisting file (id={}) metadata into database...", id);
        fileRepository.save(entityToSave);
        log.info("File (id={}) metadata was successfully persisted into database", id);
    }

    private void cleanupStoredFile(final String fileLocation) {
        try {
            fileStorageService.deleteFile(fileLocation);
        } catch (IOException deleteFileException) {
            log.warn("Unable to delete file (file location = {}) during clean-up due to exception. " +
                    "This file should be manually deleted.", fileLocation, deleteFileException);
        }
    }

}
