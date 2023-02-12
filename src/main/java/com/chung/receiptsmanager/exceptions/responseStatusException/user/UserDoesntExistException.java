package com.chung.receiptsmanager.exceptions.responseStatusException.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserDoesntExistException extends ResponseStatusException {

    public UserDoesntExistException() {
        super(HttpStatus.NOT_FOUND, "User does not exist");
    }

}
