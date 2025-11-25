package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

public interface MessageProducer {
    void sendUserAction(UserActionAvro userActionAvro);
}