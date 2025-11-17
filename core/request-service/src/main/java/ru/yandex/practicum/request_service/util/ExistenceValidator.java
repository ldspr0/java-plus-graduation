package ru.yandex.practicum.request_service.util;

public interface ExistenceValidator<T> {
    void validateExists(Long id);
}