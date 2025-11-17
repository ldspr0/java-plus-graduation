package ru.yandex.practicum.core_api.util;

public interface DataProvider<D, E> {
    D getDto(E entity);
}
