package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.ewm.stats.avro.EventSimilarityAvro;

public interface EventSimilarityService {
    void updateEventSimilarity(EventSimilarityAvro eventSimilarityAvro);
}