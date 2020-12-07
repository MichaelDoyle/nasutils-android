/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.istat;

public class ReadyNasIstatServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReadyNasIstatServiceException(String message) {
        super(message);
    }

    public ReadyNasIstatServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
