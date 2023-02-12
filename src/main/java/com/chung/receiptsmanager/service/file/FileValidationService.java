package com.chung.receiptsmanager.service.file;

import com.chung.receiptsmanager.exceptions.responseStatusException.file.ActualFileTypeDoesNotMatchFileExtensionException;
import com.chung.receiptsmanager.exceptions.responseStatusException.file.UnacceptableFileTypeException;
import com.chung.receiptsmanager.exceptions.responseStatusException.file.UnacceptableFilenameException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Service
public class FileValidationService {

    private final Pattern SAFE_FILENAME_REGEX = Pattern.compile("^[a-zA-Z0-9-_+()\\[\\] ]+(?:\\.[a-zA-Z]+)?$");
    private final MimeTypes MIME_TYPES = new MimeTypes();
    private final Set<MediaType> ALLOWLISTED_FILE_TYPES = Set.of(
            MediaType.parse("image/gif"),
            MediaType.parse("image/jpeg"),
            MediaType.parse("image/png"),
            MediaType.parse("image/tiff"),
            MediaType.parse("application/pdf")
    );

    public void validateFile(final Path toValidate)
            throws UnacceptableFileTypeException, ActualFileTypeDoesNotMatchFileExtensionException, IOException {
        if(!toValidate.toFile().isFile()) {
            throw new IllegalArgumentException(toValidate + " is not a file");
        }

        this.verifyFilenameSafe(toValidate);
        this.verifyActualFileTypeAcceptable(toValidate);

        if(this.hasFileExtension(toValidate)) {
            this.verifyFileExtensionMatchActualFileType(toValidate);
        }
    }

    public void validateFile(final Path toValidate, final Runnable cleanupOnFailure)
            throws UnacceptableFileTypeException, ActualFileTypeDoesNotMatchFileExtensionException, IOException {
        try {
            this.validateFile(toValidate);
        } catch (IOException ex) {
            log.warn("Experienced exception while trying to validate file (path={}) for security. Cleanup function " +
                    "will be run {}", toValidate, cleanupOnFailure.toString(), ex);
            cleanupOnFailure.run();
            throw ex;
        } catch (Exception ex) {
            log.warn("File (path={}) failed security validations. Cleanup function will be run {}.",
                    toValidate, cleanupOnFailure.toString(), ex);
            cleanupOnFailure.run();
            throw ex;
        }
    }

    private void verifyActualFileTypeAcceptable(final Path file) throws UnacceptableFileTypeException, IOException {
        if(file.toFile().isDirectory()) {
            throw new IllegalArgumentException(file + " is a directory, not a file");
        }

        final MediaType actualFileType = analyseFileType(file);
        final boolean isActualFileTypeUnacceptable = !this.ALLOWLISTED_FILE_TYPES.contains(actualFileType);

        if(isActualFileTypeUnacceptable) {
            throw new UnacceptableFileTypeException(actualFileType);
        }
    }

    private void verifyFileExtensionMatchActualFileType(final Path file)
            throws ActualFileTypeDoesNotMatchFileExtensionException, IOException {
        if(file.toFile().isDirectory()) {
            throw new IllegalArgumentException(file + " is a directory, not a file");
        }

        if(!hasFileExtension(file)) {
            throw new IllegalArgumentException(file + " does not have a file extension");
        }

        final MediaType actualFileType = analyseFileType(file);
        try {
            final String actualFileExtension = extractFileExtension(file);
            final var expectedFileExtensions = MIME_TYPES.forName(actualFileType.toString()).getExtensions();

            if(!expectedFileExtensions.contains(actualFileExtension)) {
                throw new ActualFileTypeDoesNotMatchFileExtensionException(
                        actualFileType, actualFileExtension, expectedFileExtensions
                );
            }
        } catch (MimeTypeException ex) {
            throw new AssertionError("Unexpected received an invalid MediaType %s"
                    .formatted(actualFileType.toString()), ex);
        }
    }

    private void verifyFilenameSafe(final Path file) {
        if(file.toFile().isDirectory()) {
            throw new IllegalArgumentException(file + " is a directory, not a file");
        }

        final String filename = file.getFileName().toString();
        if(!isFilenameSafe(filename)) {
            throw new UnacceptableFilenameException("Provided filename (%s) does not match regex %s"
                    .formatted(filename, SAFE_FILENAME_REGEX.toString())
            );
        }
    }

    private boolean hasFileExtension(final Path file) {
        return file.getFileName().toString().contains(".");
    }

    private boolean isFilenameSafe(final String filename) {
        return SAFE_FILENAME_REGEX
                .matcher(filename)
                .matches();
    }

    private MediaType analyseFileType(final Path file) throws IOException {
        if(file.toFile().isDirectory()) {
            throw new IllegalArgumentException(file + " is a directory, not a file");
        }

        try(final var fileInputStream = new BufferedInputStream(new FileInputStream(file.toFile()))) {
            return new DefaultDetector()
                    .detect(fileInputStream, new Metadata())
                    .getBaseType();
        }
    }

    private String extractFileExtension(final Path file) {
        assert file.toFile().isFile() : file + " is not a file";

        final String[] tokenizedFilename = file.getFileName().toString().split("\\.");
        assert tokenizedFilename.length <= 1 : "Expected there to be at least one full-stop for file extension";

        return tokenizedFilename[tokenizedFilename.length - 1];
    }

}
