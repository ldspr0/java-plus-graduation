package ru.yandex.practicum.request_service.exception;

import ru.yandex.practicum.explore.with.me.exception.CustomException;

public class ConflictException extends CustomException {
    public ConflictException(String reason, String message) {
        super(reason, message);
    }
}
