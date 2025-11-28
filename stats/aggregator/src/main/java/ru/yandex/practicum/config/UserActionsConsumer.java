package ru.yandex.practicum.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.service.SimilarityService;
import ru.practicum.ewm.stats.avro.UserActionAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionsConsumer {

    private final SimilarityService similarityService;

    @KafkaListener(
            topics = "#{kafkaProperties.consumer.topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeUserAction(UserActionAvro message) {
        try {
            log.info("consumeUserAction Kafka: {}", message);
            similarityService.processUserAction(message);
        } catch (Exception e) {
            log.error("Error processing user action, skipping message: {}", message, e);
        }
    }
}