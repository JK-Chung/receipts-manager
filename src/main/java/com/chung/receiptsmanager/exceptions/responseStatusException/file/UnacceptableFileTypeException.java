package com.chung.receiptsmanager.exceptions.responseStatusException.file;

import org.apache.tika.mime.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnacceptableFileTypeException extends ResponseStatusException {

    public UnacceptableFileTypeException(final MediaType unacceptableMediaType) {
        super(HttpStatus.BAD_REQUEST, "%s is not allowed as a file type".formatted(unacceptableMediaType.getBaseType()));
    }

}
