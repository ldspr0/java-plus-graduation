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
import java.util.Map;

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
        double newWeight = convertActionType(action.getActionType()); // ← double
        Instant timestamp = action.getTimestamp();

        log.info("PROCESSING ACTION: userId={}, eventId={}, newWeight={}", userId, eventId, newWeight);

        Map<Long, Double> userMap = weights.computeIfAbsent(eventId, e -> new HashMap<>());
        double oldWeight = userMap.getOrDefault(userId, 0.0);

        log.info("OLD WEIGHT: {}, NEW WEIGHT: {}", oldWeight, newWeight);

        if (newWeight <= oldWeight) {
            log.debug("Update is not required for: userId={}, eventId={}, weight={} <= oldWeight={}",
                    userId, eventId, newWeight, oldWeight);
            return;
        }

        log.info("PROCEEDING WITH CALCULATION...");

        userMap.put(userId, newWeight);

        double oldSum = eventWeightsSum.getOrDefault(eventId, 0.0);
        double diff = newWeight - oldWeight;
        double updatedSum = oldSum + diff;
        eventWeightsSum.put(eventId, updatedSum);

        updateSMinForAllPairs(eventId, userId, oldWeight, newWeight);

        weights.keySet().stream()
                .filter(otherEventId -> !otherEventId.equals(eventId))
                .forEach(otherEventId -> updatePairSimilarity(eventId, otherEventId, timestamp));
    }

    private void updateSMinForAllPairs(long updatedEventId, long userId, double oldWeight, double newWeight) {
        for (Long otherEventId : weights.keySet()) {
            if (otherEventId.equals(updatedEventId)) {
                continue;
            }

            Map<Long, Double> otherUserMap = weights.get(otherEventId);
            if (otherUserMap.containsKey(userId)) {
                double otherWeight = otherUserMap.get(userId);

                double oldMin = Math.min(oldWeight, otherWeight);
                double newMin = Math.min(newWeight, otherWeight);
                double diff = newMin - oldMin;

                if (diff != 0) {
                    double currentSMin = minWeightsMatrix.get(updatedEventId, otherEventId);
                    double updatedSMin = currentSMin + diff;
                    minWeightsMatrix.put(updatedEventId, otherEventId, updatedSMin);

                    log.debug("Updated S_min for pair ({}, {}): {} -> {}",
                            updatedEventId, otherEventId, currentSMin, updatedSMin);
                }
            }
        }
    }

    private void updatePairSimilarity(long eventA, long eventB, Instant timestamp) {
        double sMin = minWeightsMatrix.get(eventA, eventB);
        double sA = eventWeightsSum.getOrDefault(eventA, 0.0);
        double sB = eventWeightsSum.getOrDefault(eventB, 0.0);

        if (sA == 0 || sB == 0) {
            log.debug("Zero sum: (sA={}, sB={}) for events: {} and {}", sA, sB, eventA, eventB);
            return;
        }

        double similarity = sMin / Math.sqrt(sA * sB);

        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        EventSimilarityAvro similarityMsg = EventSimilarityAvro.newBuilder()
                .setEventA(first)
                .setEventB(second)
                .setScore(similarity)
                .setTimestamp(timestamp)
                .build();

        kafkaTemplate.send(props.getProducer().getTopic(), similarityMsg);

        log.debug("Similarity updated for (A={}, B={}) => {}", first, second, similarity);
    }

    private double convertActionType(ActionTypeAvro actionType) {
        return switch (actionType) {
            case VIEW -> 0.4;
            case REGISTER -> 0.8;
            case LIKE -> 1.0;
        };
    }
}