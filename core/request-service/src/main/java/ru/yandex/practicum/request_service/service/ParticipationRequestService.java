package ru.yandex.practicum.request_service.service;

import ru.yandex.practicum.request_service.model.CancelParticipationRequest;
import ru.yandex.practicum.request_service.model.NewParticipationRequest;
import ru.yandex.practicum.request_service.model.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> find(Long userId);

    ParticipationRequestDto create(NewParticipationRequest newParticipationRequest);

    ParticipationRequestDto cancel(CancelParticipationRequest cancelParticipationRequest);

    boolean isParticipantApproved(Long userId, Long eventId);
}
