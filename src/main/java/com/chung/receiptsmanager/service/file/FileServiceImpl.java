package com.chung.receiptsmanager.service.file;

import com.chung.receiptsmanager.dto.FileDto;
import com.chung.receiptsmanager.entity.FileEntity;
import com.chung.receiptsmanager.entity.UserEntity;
import com.chung.receiptsmanager.repository.file.FileRepository;
import com.chung.receiptsmanager.service.file.helper.VolatileLocalFileStorage;
import com.chung.receiptsmanager.service.security.CurrentAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final VolatileLocalFileStorage volatileLocalFileStorage;
    private final FileValidationService fileValidationService;
    private final CurrentAuthenticationService currentAuthenticationService;
    private final Converter<FileEntity, FileDto> fileEntityFileDtoMapper;

    public FileServiceImpl(
            FileRepository fileRepository,
            FileStorageService fileStorageService,
            VolatileLocalFileStorage volatileLocalFileStorage,
            FileValidationService fileValidationService,
            CurrentAuthenticationService currentAuthenticationService,
            Converter<FileEntity, FileDto> fileEntityFileDtoMapper) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
        this.volatileLocalFileStorage = volatileLocalFileStorage;
        this.fileValidationService = fileValidationService;
        this.currentAuthenticationService = currentAuthenticationService;
        this.fileEntityFileDtoMapper = fileEntityFileDtoMapper;
    }

    @Override
    public FileDto storeFileAndMetadata(final InputStream toStoreInputStream, final String userProvidedFilename) throws IOException {
        final Path toSaveServerCopy = this.volatileLocalFileStorage.tempStoreFileOnServer(toStoreInputStream);

        this.fileValidationService.validateFile(toSaveServerCopy, () -> {
            try {
                Files.delete(toSaveServerCopy);
            } catch (IOException ex) {
                log.warn("Unable to delete file {} during cleanup after validation failure. Should be deleted " +
                        "manually", toSaveServerCopy, ex);
            }
        });

        final String fileLocation = fileStorageService.storeFile(toSaveServerCopy);
        final UUID id = UUID.randomUUID();
        log.info("File (id={}, file-location={}) was successfully stored and validated. Now persiting metadata...",
                id, fileLocation);

        final var saved = persistFileMetadata(toSaveServerCopy, id, fileLocation, userProvidedFilename, () -> {
            cleanupStoredFile(fileLocation);
            try {
                Files.delete(toSaveServerCopy);
            } catch (IOException ex) {
                log.warn("Unable to delete file {} during cleanup after validation failure. Should be deleted " +
                        "manually", toSaveServerCopy, ex);
            }
        });
        return fileEntityFileDtoMapper.convert(saved);
    }

    private FileEntity persistFileMetadata(
            final Path file,
            final UUID id,
            final String fileLocation,
            final String userProvidedFilename,
            final Runnable cleanupOnFailure
    ) {
        try {
            return persistFileMetadata(file, id, fileLocation, userProvidedFilename);
        } catch (Exception ex) {
            log.error("Exception when trying to persist metadata on saved file (id={}, file-location={}). " +
                    "Will run cleanup function {}.", id, fileLocation, cleanupOnFailure.toString(), ex);
            cleanupOnFailure.run();
            throw ex;
        }
    }

    private FileEntity persistFileMetadata(final Path file, final UUID id, final String fileLocation, final String userProvidedFilename) {
        final UserEntity owner = currentAuthenticationService.getCurrentlyAuthenticatedUserEntity();
        log.info("User (id={}, username={}) will be identified as the owner of the file (id={}, file-location={})",
                owner.getId(), owner.getUsername(), id, fileLocation);

        final var entityToSave = FileEntity.builder()
                .id(id)
                .owner(owner)
                .userProvidedFileName(userProvidedFilename)
                .fileLocation(fileLocation)
                .build();

        log.info("Persisting file (id={}) metadata into database...", id);
        final var saved = fileRepository.save(entityToSave);
        log.info("File (id={}) metadata was successfully persisted into database", id);
        return saved;
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
