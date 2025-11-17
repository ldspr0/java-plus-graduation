package ru.yandex.practicum.user_service.util;

public interface DataProvider<D, E> {
    D getDto(E entity);
}
