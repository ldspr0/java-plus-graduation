package ru.yandex.practicum.request_service.service;

import ru.yandex.practicum.core_api.model.request.CancelParticipationRequest;
import ru.yandex.practicum.core_api.model.request.NewParticipationRequest;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> find(Long userId);

    ParticipationRequestDto create(NewParticipationRequest newParticipationRequest);

    ParticipationRequestDto cancel(CancelParticipationRequest cancelParticipationRequest);

    boolean isParticipantApproved(Long userId, Long eventId);
}
