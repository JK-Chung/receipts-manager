package com.chung.receiptsmanager.exceptions.responseStatusException.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnacceptableFilenameException extends ResponseStatusException {

    public UnacceptableFilenameException(final String userFriendlyMsg) {
        super(HttpStatus.BAD_REQUEST, userFriendlyMsg);
    }

}
