package ru.yandex.practicum.event_service.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.interfaces.EventInterface;
import ru.yandex.practicum.core_api.model.event.AdminEventFilter;
import ru.yandex.practicum.core_api.model.event.EventPublicSort;
import ru.yandex.practicum.core_api.model.event.PublicEventParam;
import ru.yandex.practicum.core_api.model.event.dto.*;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;
import ru.yandex.practicum.core_api.util.StatSaver;
import ru.yandex.practicum.event_service.service.event.EventAdminService;
import ru.yandex.practicum.event_service.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventController implements EventInterface {
    private final String className = this.getClass().getSimpleName();
    private final EventAdminService service;
    private final EventService eventsService;
    private final StatSaver statSaver;
    @Autowired
    private HttpServletRequest request;

    @Override
    @GetMapping("/admin/events")
    public List<EventFullDto> searchEvents(@ModelAttribute AdminEventSearchRequestDto req) {
        log.trace("{}: searchEvents() call with dto: {}", className, req);
        Pageable page = PageRequest.of(req.getFrom() / req.getSize(), req.getSize());
        AdminEventFilter f = new AdminEventFilter(
                req.getUsers(), req.getStates(), req.getCategories(),
                req.getRangeStart(), req.getRangeEnd());
        return service.search(f, page);
    }

    @Override
    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable @PositiveOrZero @NotNull Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequestDto dto) {
        log.trace("{}: updateEvent() call with eventId: {}, dto: {}", className, eventId, dto);
        return service.update(eventId, dto);
    }

    @Override
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable @PositiveOrZero @NotNull Long userId,
                                    @RequestBody @Valid NewEventDto event) {
        log.trace("{}: createEvent() call with userId: {}, event: {}", className, userId, event);
        return eventsService.createEvent(userId, event);
    }

    @Override
    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdForUser(@PathVariable @PositiveOrZero @NotNull Long userId,
                                            @PathVariable @PositiveOrZero @NotNull Long eventId) {
        log.trace("{}: getEventByIdForUser() call with userId: {}, eventId: {}", className, userId, eventId);
        return eventsService.getPrivateEventById(userId, eventId);
    }

    @Override
    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable @PositiveOrZero @NotNull Long userId,
                                    @PathVariable @PositiveOrZero @NotNull Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEvent) {
        log.trace("{}: updateEvent() call with userId: {}, eventId: {}, updateEvent: {}",
                className, userId, eventId, updateEvent);
        return eventsService.updateEvent(userId, eventId, updateEvent);
    }

    @Override
    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable @PositiveOrZero @NotNull Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size) {
        log.trace("{}: getEvents() call with userId: {}, from: {}, size: {}", className, userId, from, size);
        return eventsService.getEventsByUser(userId, from, size);
    }

    @Override
    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) EventPublicSort sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        statSaver.save(request, className);

        PublicEventParam publicEventParam = new PublicEventParam();
        publicEventParam.setText(Objects.requireNonNullElse(text, ""));
        publicEventParam.setCategories(categories);
        publicEventParam.setPaid(paid);
        publicEventParam.setRangeStart(rangeStart);
        publicEventParam.setRangeEnd(rangeEnd);
        publicEventParam.setOnlyAvailable(onlyAvailable);
        publicEventParam.setSort(sort);
        publicEventParam.setFrom(from);
        publicEventParam.setSize(size);
        log.trace("{}: getEvents() call with publicEventParam: {}", className, publicEventParam);

        return eventsService.getPublicEvents(publicEventParam);
    }

    @Override
    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable @PositiveOrZero @NotNull Long eventId) {
        statSaver.save(request, className);
        log.trace("{}: getEventByIdForUser() call with eventId: {}", className, eventId);
        return eventsService.getPublicEventById(eventId);
    }




}
