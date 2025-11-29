package ru.yandex.practicum.core_api.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.model.event.EventPublicSort;
import ru.yandex.practicum.core_api.model.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventInterface {

    // Admin endpoints
    @GetMapping("/admin/events")
    List<EventFullDto> searchEvents(@ModelAttribute AdminEventSearchRequestDto req);

    @PatchMapping("/admin/events/{eventId}")
    EventFullDto updateEvent(@PathVariable("eventId") @PositiveOrZero @NotNull Long eventId,
                             @RequestBody @Valid UpdateEventAdminRequestDto dto);

    // Private endpoints
    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    List<EventShortDto> getEvents(@PathVariable("userId") @PositiveOrZero @NotNull Long userId,
                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(value = "size", defaultValue = "10") @Positive int size);

    @PostMapping("/users/{userId}/events")
    EventFullDto createEvent(@PathVariable("userId") @PositiveOrZero @NotNull Long userId,
                             @RequestBody @Valid NewEventDto event);

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto getEventByIdForUser(@PathVariable("userId") @PositiveOrZero @NotNull Long userId,
                                     @PathVariable("eventId") @PositiveOrZero @NotNull Long eventId);


    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto updateEvent(@PathVariable("userId") @PositiveOrZero @NotNull Long userId,
                             @PathVariable("eventId") @PositiveOrZero @NotNull Long eventId,
                             @RequestBody @Valid UpdateEventUserRequest updateEvent);


    // Public endpoints
    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) Boolean paid,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                  @RequestParam(required = false) EventPublicSort sort,
                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  HttpServletRequest request);

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto getEventById(@PathVariable("eventId") @PositiveOrZero @NotNull Long eventId, @RequestHeader("X-EWM-USER-ID") long userId);

    @GetMapping("/events/{eventId}/internal")
    EventFullDto getEventByIdInternal(@PathVariable("eventId") @PositiveOrZero @NotNull Long eventId);
}
