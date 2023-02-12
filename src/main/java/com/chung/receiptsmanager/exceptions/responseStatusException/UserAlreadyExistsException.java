package com.chung.receiptsmanager.exceptions.responseStatusException;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistsException extends ResponseStatusException {

    public UserAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "User already exists with given username and/or email-address");
    }

}
