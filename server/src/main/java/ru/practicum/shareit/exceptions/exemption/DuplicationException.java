package ru.practicum.shareit.exceptions.exemption;

public class DuplicationException extends RuntimeException {
    public DuplicationException(String message) {
        super(message);
    }
}