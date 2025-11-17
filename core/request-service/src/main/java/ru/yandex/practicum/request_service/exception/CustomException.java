package ru.yandex.practicum.request_service.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String reason;

    public CustomException(String reason, String message) {
        super(message);
        this.reason = reason;
    }
}
