package com.chung.receiptsmanager.exceptions.responseStatusException.file;

import org.apache.tika.mime.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public class ActualFileTypeDoesNotMatchFileExtensionException extends ResponseStatusException {

    public ActualFileTypeDoesNotMatchFileExtensionException(
            final MediaType actualFileType,
            final String actualFileExtension,
            final Collection<String> expectedFileExtensions
    ) {
        super(HttpStatus.BAD_REQUEST, ("File was detected to actually be %s. This does not match the provided file " +
                "extension of %s. Expected file extensions are: %s")
                .formatted(actualFileType.toString(), actualFileExtension, expectedFileExtensions.toString()));
    }

}
