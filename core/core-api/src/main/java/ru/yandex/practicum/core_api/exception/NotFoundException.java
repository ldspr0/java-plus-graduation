package ru.yandex.practicum.core_api.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(String reason, String message) {
        super(reason, message);
    }
}
