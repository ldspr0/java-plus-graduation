package ru.yandex.practicum.core_api.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String reason;

    public CustomException(String reason, String message) {
        super(message);
        this.reason = reason;
    }
}
