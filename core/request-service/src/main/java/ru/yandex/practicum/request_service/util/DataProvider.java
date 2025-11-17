package ru.yandex.practicum.request_service.util;

public interface DataProvider<D, E> {
    D getDto(E entity);
}
