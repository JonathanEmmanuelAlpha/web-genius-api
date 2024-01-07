package com.webgenius.webgeniusapi.config;

import com.webgenius.webgeniusapi.utils.Message;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Message handleNotFound(final HttpServletRequest request, final Exception error) {
        return Message.from("404 Not Found");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Message handleAccessDenied(final HttpServletRequest request, final Exception error) {
        return Message.from("Permission denied");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public Message handleInternalError(final HttpServletRequest request, final Exception error) {
        return Message.from(error.getMessage());
    }
}
