package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.exemption.AuthorizationException;
import ru.practicum.shareit.exceptions.exemption.DuplicationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return ErrorResponse.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(404)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExistsException(DuplicationException e) {
        return ErrorResponse.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(409)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAuthorizationException(AuthorizationException e) {
        return ErrorResponse.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(400).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return ErrorResponse.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(500).build();
    }
}
