package ru.yandex.practicum.user_service.exception;

public class ConflictException extends CustomException {
    public ConflictException(String reason, String message) {
        super(reason, message);
    }
}
