package ru.yandex.practicum.core_api.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestCount;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;

import java.util.List;

public interface RequestInterface {

    // Private endpoints
    @GetMapping("/users/{userId}/requests")
    List<ParticipationRequestDto> find(@PathVariable("userId")
                                       @NotNull(message = "must not be null")
                                       @PositiveOrZero(message = "must be positive or zero")
                                       Long userId);

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto create(@PathVariable("userId")
                                   @NotNull(message = "must not be null")
                                   @PositiveOrZero(message = "must be positive or zero")
                                   Long userId,
                                   @RequestParam("eventId")
                                   @NotNull(message = "must not be null")
                                   @PositiveOrZero(message = "must be positive or zero")
                                   Long eventId);

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancel(@PathVariable("userId")
                                   @NotNull(message = "must not be null")
                                   @PositiveOrZero(message = "must be positive or zero")
                                   Long userId,
                                   @PathVariable("requestId")
                                   @NotNull(message = "must not be null")
                                   @PositiveOrZero(message = "must be positive or zero")
                                   Long requestId);

    @GetMapping("/users/{userId}/events/{eventId}/participation")
    boolean isParticipantApproved(@PathVariable("userId")
                                  @NotNull @PositiveOrZero Long userId,
                                  @PathVariable("eventId")
                                  @NotNull @PositiveOrZero Long eventId);

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    EventRequestStatusUpdateResult updateEventRequestStatus(@PathVariable("userId")
                                                            @PositiveOrZero
                                                            @NotNull
                                                            Long userId,
                                                            @PathVariable("eventId")
                                                            @PositiveOrZero
                                                            @NotNull
                                                            Long eventId,
                                                            @RequestBody
                                                            @Valid
                                                            EventRequestStatusUpdateRequest updateRequest);
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    List<ParticipationRequestDto> getEventParticipationRequestsByUser(@PathVariable("userId")
                                                                      @PositiveOrZero
                                                                      @NotNull
                                                                      Long userId,
                                                                      @PathVariable("eventId")
                                                                      @PositiveOrZero
                                                                      @NotNull
                                                                      Long eventId);

    @GetMapping("/requests/count")
    @ResponseStatus(HttpStatus.OK)
    List<EventRequestCount> countGroupByEventId(@RequestParam("eventIds")
                                                @NotNull(message = "must not be null")
                                                List<Long> eventIds);

}
