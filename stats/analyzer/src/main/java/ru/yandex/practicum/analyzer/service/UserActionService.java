package ru.yandex.practicum.analyzer.service;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionService {
    void updateUserAction(UserActionAvro userActionAvro);
}