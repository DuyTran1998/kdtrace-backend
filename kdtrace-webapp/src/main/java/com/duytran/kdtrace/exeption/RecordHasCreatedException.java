package com.duytran.kdtrace.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CREATED)
public class RecordHasCreatedException extends RuntimeException {
    public RecordHasCreatedException(String exception) {
        super(exception);
    }
}