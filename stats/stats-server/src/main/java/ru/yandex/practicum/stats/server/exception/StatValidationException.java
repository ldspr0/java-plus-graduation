package ru.yandex.practicum.stats.server.exception;

public class StatValidationException extends RuntimeException {
    public StatValidationException(String message) {
        super(message);
    }
}
