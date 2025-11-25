package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.entity.UserAction;

import java.util.List;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    UserAction findByUserIdAndEventId(Long userId, Long eventId);

    List<UserAction> findByUserId(Long userId);

    List<UserAction> findByEventId(Long eventId);
}