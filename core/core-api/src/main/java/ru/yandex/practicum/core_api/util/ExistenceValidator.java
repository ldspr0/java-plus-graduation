package ru.yandex.practicum.core_api.util;

public interface ExistenceValidator<T> {
    void validateExists(Long id);
}