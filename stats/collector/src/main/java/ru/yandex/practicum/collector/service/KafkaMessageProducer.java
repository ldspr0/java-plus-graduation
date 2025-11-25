package ru.yandex.practicum.collector.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import ru.yandex.practicum.collector.config.KafkaProperties;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducer implements MessageProducer {

    private final KafkaTemplate<String, UserActionAvro> kafkaTemplate;
    private final KafkaProperties properties;

    @Override
    public void sendUserAction(UserActionAvro userActionAvro) {
        kafkaTemplate.send(properties.getUserActionsTopic(), userActionAvro);
    }
}