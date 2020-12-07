/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

public class UnsupportedOperationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnsupportedOperationException(String message) {
        super(message);
    }

    public UnsupportedOperationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
