package ru.yandex.practicum.request_service.service;

import ru.yandex.practicum.core_api.model.event.dto.EventRequestCount;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.core_api.model.request.CancelParticipationRequest;
import ru.yandex.practicum.core_api.model.request.NewParticipationRequest;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> find(Long userId);

    ParticipationRequestDto create(NewParticipationRequest newParticipationRequest);

    ParticipationRequestDto cancel(CancelParticipationRequest cancelParticipationRequest);

    List<ParticipationRequestDto> getEventParticipationRequestsByUser(long userId, long eventId);

    boolean isParticipantApproved(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestStatus(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<EventRequestCount> countGroupByEventId(List<Long> eventIds);
}
