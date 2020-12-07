/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.http;

public class HttpClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
