package com.duytran.kdtrace.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MessagingExceptionHandler extends RuntimeException {
    public MessagingExceptionHandler(String exception){
        super(exception);
    }
}