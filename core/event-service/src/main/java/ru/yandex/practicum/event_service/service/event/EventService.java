package ru.yandex.practicum.event_service.service.event;

import ru.yandex.practicum.event_service.model.Event;
import ru.yandex.practicum.core_api.model.event.EventStatistics;
import ru.yandex.practicum.core_api.model.event.PublicEventParam;
import ru.yandex.practicum.core_api.model.event.dto.EventFullDto;
import ru.yandex.practicum.core_api.model.event.dto.EventShortDto;
import ru.yandex.practicum.core_api.model.event.dto.EventViewsParameters;
import ru.yandex.practicum.core_api.model.event.dto.NewEventDto;
import ru.yandex.practicum.core_api.model.event.dto.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EventService {
    EventFullDto createEvent(long userId, NewEventDto eventDto);

    EventFullDto getPrivateEventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEvent);

    EventFullDto getPublicEventById(long eventId);

    EventFullDto getEventByIdInternal(long eventId);

    List<EventShortDto> getEventsByUser(long userId, int from, int count);

    Map<Long, Long> getEventViews(EventViewsParameters params);


    List<EventShortDto> getPublicEvents(PublicEventParam params);

    EventStatistics getEventStatistics(List<Event> events, LocalDateTime startStats, LocalDateTime endStats);
}
