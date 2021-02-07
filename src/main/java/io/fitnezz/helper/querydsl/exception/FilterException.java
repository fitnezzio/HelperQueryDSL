package io.fitnezz.helper.querydsl.exception;

import lombok.Getter;

@Getter
public class FilterException extends RuntimeException {

    public FilterException(String message) {
        super(message);
    }

    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }

}