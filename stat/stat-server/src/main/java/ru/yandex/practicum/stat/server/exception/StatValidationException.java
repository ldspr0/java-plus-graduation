package ru.yandex.practicum.stat.server.exception;

public class StatValidationException extends RuntimeException {
    public StatValidationException(String message) {
        super(message);
    }
}
