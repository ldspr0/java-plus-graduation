package ru.yandex.practicum.core_api.exception;

public class ForbiddenException extends CustomException {
    public ForbiddenException(String reason, String message) {
        super(reason, message);
    }
}
