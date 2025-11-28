package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.analyzer.entity.RecommendedEvent;
import stats.service.dashboard.Recommendations;

import java.util.List;


public interface RecommendationService {

    List<RecommendedEvent> getSimilarEvents(Recommendations.SimilarEventsRequestProto request);

    List<RecommendedEvent> getRecommendationsForUser(Recommendations.UserPredictionsRequestProto request);

    List<RecommendedEvent> getInteractionsCount(Recommendations.InteractionsCountRequestProto request);
}
