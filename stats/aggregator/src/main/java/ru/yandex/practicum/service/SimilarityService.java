package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaProperties;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class SimilarityService {

    private final Map<Long, Map<Long, Double>> weights = new HashMap<>();
    private final Map<Long, Double> eventWeightsSum = new HashMap<>();
    private final MinWeightsMatrix minWeightsMatrix = new MinWeightsMatrix();

    private final KafkaTemplate<String, EventSimilarityAvro> kafkaTemplate;
    private final KafkaProperties props;

    public SimilarityService(KafkaTemplate<String, EventSimilarityAvro> kafkaTemplate,
                             KafkaProperties props) {
        this.kafkaTemplate = kafkaTemplate;
        this.props = props;
    }

    public void processUserAction(UserActionAvro action) {
        long userId = action.getUserId();
        long eventId = action.getEventId();
        double newWeight = convertActionType(action.getActionType());
        Instant timestamp = action.getTimestamp();

        log.info("PROCESSING ACTION: userId={}, eventId={}, newWeight={}", userId, eventId, newWeight);
        Map<Long, Double> userMap = weights.computeIfAbsent(eventId, e -> new HashMap<>());
        double oldWeight = userMap.getOrDefault(userId, 0.0);
        if (newWeight <= oldWeight) {
            log.debug("Update is not required for: userId={}, eventId={}, weight={} <= oldWeight={}",
                    userId, eventId, newWeight, oldWeight);
            return;
        }
        log.info("OLD WEIGHT: {}, NEW WEIGHT: {}", oldWeight, newWeight);

        log.info("PROCEEDING WITH CALCULATION...");

        // Обновляем вес пользователя
        userMap.put(userId, newWeight);

        // Обновляем сумму весов мероприятия
        updateEventWeightSum(eventId, oldWeight, newWeight);

        updateMinWeightMatrix(eventId, userId, oldWeight, newWeight);

        // Пересчитываем и отправляем обновления только для нужных пар
        recalculateAndSendSimilarities(eventId, timestamp, oldWeight, userId);


    }

    private void updateMinWeightMatrix(long eventId, long userId, double oldWeight, double newWeight) {
        for (Long eachEvent : weights.keySet()) {
            if (eachEvent == eventId) {
                continue;
            }
            if (weights.get(eachEvent).get(userId) == null || weights.get(eachEvent).get(userId) == 0.0) {
                continue;
            }
            double otherEventWeight = weights.get(eachEvent).get(userId);
            if (otherEventWeight <= oldWeight) {
                continue;
            }
            log.info("otherEventWeight event: {} ", otherEventWeight);
            double oldMin = minWeightsMatrix.get(eventId, eachEvent);
            log.info("oldMin event: {} ", oldMin);
            double diff = Math.min(otherEventWeight, newWeight) - oldWeight;
            log.info("diff event: {} ", diff);
            minWeightsMatrix.put(eventId, eachEvent, oldMin + diff);

        }
        log.info("Updated min weight matrix for event: {} ", eventId);
    }

    private void updateEventWeightSum(long eventId, double oldWeight, double newWeight) {
        double oldSum = eventWeightsSum.getOrDefault(eventId, 0.0);
        double diff = newWeight - oldWeight;
        double updatedSum = oldSum + diff;
        eventWeightsSum.put(eventId, updatedSum);
        log.info("Updated weight sum for event {}: {} -> {}", eventId, oldSum, updatedSum);
    }

    private void recalculateAndSendSimilarities(long eventId, Instant timestamp, double oldWeight, long userId) {
        log.info("recalculateAndSendSimilarities");
        Set<String> sentPairs = new HashSet<>();

        // Для каждой пары пересчитываем S_min и similarity
        for (Long otherEventId : weights.keySet()) {
            log.info("otherEventId: {}", otherEventId);
            if (otherEventId == eventId) {
                continue;
            }
            if (weights.get(otherEventId).get(userId) == null || weights.get(otherEventId).get(userId) == 0.0) {
                log.info("weigh : {} ",weights.get(otherEventId).get(userId));
                log.info("lol");
                continue;
            }
            String pairKey = getPairKey(eventId, otherEventId);
            log.info("pairKey: {}", pairKey);

            if (sentPairs.contains(pairKey)) {
                log.debug("Pair {} already sent, skipping", pairKey);
                continue;
            }

            log.info("Processing pair: ({}, {})", eventId, otherEventId);

            double similarity = calculateSimilarity(eventId, otherEventId);

            sendSimilarityUpdate(eventId, otherEventId, similarity, timestamp);
            sentPairs.add(pairKey);

        }

    }

    private double calculateSimilarity(long eventA, long eventB) {

        double sMin = minWeightsMatrix.get(eventA, eventB);
        log.info("eventA : {}", eventA);
        log.info("eventB : {}", eventB);
        log.info("sMin : {}", sMin);
        return calculateSimilarityWithSMin(eventA, eventB, sMin);
    }

    private double calculateSimilarityWithSMin(long eventA, long eventB, double sMin) {
        double sA = eventWeightsSum.getOrDefault(eventA, 0.0);
        double sB = eventWeightsSum.getOrDefault(eventB, 0.0);

        log.info("Calculating similarity for ({}, {}): S_min={}, S_A={}, S_B={}",
                eventA, eventB, sMin, sA, sB);

        if (sA == 0 || sB == 0) {
            log.info("One of event sums is zero, returning 0");
            return 0.0;
        }

        double similarity = sMin / Math.sqrt(sA * sB);

        log.info("Similarity calculated: {} / sqrt({} * {}) = {}", sMin, sA, sB, similarity);
        return similarity;
    }

    private void sendSimilarityUpdate(long eventA, long eventB, double similarity, Instant timestamp) {
        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        EventSimilarityAvro similarityMsg = EventSimilarityAvro.newBuilder()
                .setEventA(first)
                .setEventB(second)
                .setScore(similarity)
                .setTimestamp(timestamp)
                .build();

        log.info("SENDING similarity message: eventA={}, eventB={}, score={}", first, second, similarity);

        try {
            kafkaTemplate.send(props.getProducer().getTopic(), similarityMsg);
            log.info("Similarity SENT for (A={}, B={}) => {}", first, second, similarity);
        } catch (Exception e) {
            log.error("Failed to send similarity message for (A={}, B={}): {}", first, second, e.getMessage());
        }
    }

    private String getPairKey(long eventA, long eventB) {
        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);
        return first + "_" + second;
    }

    private double convertActionType(ActionTypeAvro actionType) {
        return switch (actionType) {
            case VIEW -> 0.4;
            case REGISTER -> 0.8;
            case LIKE -> 1.0;
        };
    }
}