package com.chung.receiptsmanager.exceptions;

public class DatabaseConversionException extends RuntimeException {

    public DatabaseConversionException(final String serverMessage) {
        super(serverMessage);
    }

}
