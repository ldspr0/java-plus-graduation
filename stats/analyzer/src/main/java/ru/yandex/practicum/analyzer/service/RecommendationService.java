package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.analyzer.entity.RecommendedEvent;
import ru.yandex.practicum.ewm.stats.proto.Recommendations;

import java.util.List;


public interface RecommendationService {

    List<RecommendedEvent> getSimilarEvents(Recommendations.SimilarEventsRequestProto request);

    List<RecommendedEvent> getRecommendationsForUser(Recommendations.UserPredictionsRequestProto request);

    List<RecommendedEvent> getInteractionsCount(Recommendations.InteractionsCountRequestProto request);
}
