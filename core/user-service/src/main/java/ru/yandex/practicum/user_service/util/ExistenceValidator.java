package ru.yandex.practicum.user_service.util;

public interface ExistenceValidator<T> {
    void validateExists(Long id);
}