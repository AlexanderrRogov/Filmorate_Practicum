package org.home.yandex.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("400 Bad Request", e);
        return new ErrorResponse(
                "error: 400",
                "description: " + e.getMessage() + "."
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("404 Not Found", e);
        return new ErrorResponse(
                "error: 404",
                "description: " + e.getMessage() + "."
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerSideException(final ServerSideException e) {
        log.error("500 Internal Server Error", e);
        return new ErrorResponse(
                "error: 500",
                "description: " + e.getMessage() + "."
        );
    }

}