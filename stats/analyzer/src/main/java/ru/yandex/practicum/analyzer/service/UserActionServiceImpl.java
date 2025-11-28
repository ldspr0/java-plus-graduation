package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.analyzer.entity.UserAction;
import ru.yandex.practicum.analyzer.repository.UserActionRepository;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionServiceImpl implements UserActionService {

    private final UserActionRepository userActionRepository;

    @Override
    public void updateUserAction(UserActionAvro userActionAvro) {
        long userId = userActionAvro.getUserId();
        long eventId = userActionAvro.getEventId();
        double newWeight = convertWeight(userActionAvro.getActionType());
        Instant ts = userActionAvro.getTimestamp();

        UserAction userAction = userActionRepository.findByUserIdAndEventId(userId, eventId);
        if (userAction == null) {
            userAction = new UserAction();
            userAction.setUserId(userId);
            userAction.setEventId(eventId);
            userAction.setMaxWeight(newWeight);
            userAction.setLastInteraction(ts);
            userActionRepository.save(userAction);
            return;
        }

        if (newWeight > userAction.getMaxWeight()) {
            userAction.setMaxWeight(newWeight);
        }

        if (ts.isAfter(userAction.getLastInteraction())) {
            userAction.setLastInteraction(ts);
        }
        userActionRepository.save(userAction);
    }

    private double convertWeight(ActionTypeAvro actionType) {
        return switch (actionType) {
            case REGISTER -> 0.8;
            case LIKE -> 1;
            default -> 0.4;
        };
    }
}