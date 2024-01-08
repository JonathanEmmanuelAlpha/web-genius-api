package com.webgenius.webgeniusapi.config;

import com.webgenius.webgeniusapi.utils.Message;
import com.webgenius.webgeniusapi.utils.Response;
import com.webgenius.webgeniusapi.utils.ResponseType;
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
    public Response handleNotFound(final HttpServletRequest request, final Exception error) {
        return Response.from("404 Not Found", ResponseType.ERROR);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Response handleAccessDenied(final HttpServletRequest request, final Exception error) {
        return Response.from("Permission denied", ResponseType.ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public Response handleInternalError(final HttpServletRequest request, final Exception error) {
        return Response.from(error.getMessage(), ResponseType.ERROR);
    }
}
