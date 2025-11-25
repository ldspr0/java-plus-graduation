package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionService {
    void updateUserAction(UserActionAvro userActionAvro);
}