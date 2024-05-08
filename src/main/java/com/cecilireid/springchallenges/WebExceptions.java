package com.cecilireid.springchallenges;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

public class WebExceptions {
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleHttpNotFoundErrorException(HttpClientErrorException e) {
        return e.getMessage() + ": Please try again.";
    }
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleHttpBadRequestErrorException(HttpClientErrorException e) {
        return e.getMessage();
    }
}
