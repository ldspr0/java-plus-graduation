package ru.yandex.practicum.core_api.exception;

public class ConflictException extends CustomException {
    public ConflictException(String reason, String message) {
        super(reason, message);
    }
}
