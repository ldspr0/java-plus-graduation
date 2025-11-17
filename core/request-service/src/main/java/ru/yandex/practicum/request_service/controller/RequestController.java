package ru.yandex.practicum.request_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.interfaces.RequestInterface;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.core_api.model.request.CancelParticipationRequest;
import ru.yandex.practicum.core_api.model.request.NewParticipationRequest;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;
import ru.yandex.practicum.request_service.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController implements RequestInterface {
    private final String className = this.getClass().getSimpleName();
    private final ParticipationRequestService service;

    @Override
    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> find(@PathVariable
                                              @NotNull(message = "must not be null")
                                              @PositiveOrZero(message = "must be positive or zero")
                                              Long userId) {
        log.trace("{}: find() call with userId: {}", className, userId);
        return service.find(userId);
    }

    @Override
    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable
                                          @NotNull(message = "must not be null")
                                          @PositiveOrZero(message = "must be positive or zero")
                                          Long userId,
                                          @RequestParam
                                          @NotNull(message = "must not be null")
                                          @PositiveOrZero(message = "must be positive or zero")
                                          Long eventId) {
        log.trace("{}: create() call with userId: {}, eventId: {}", className, userId, eventId);

        NewParticipationRequest newParticipationRequest = NewParticipationRequest.builder()
                .userId(userId)
                .eventId(eventId)
                .build();
        return service.create(newParticipationRequest);
    }

    @Override
    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable
                                          @NotNull(message = "must not be null")
                                          @PositiveOrZero(message = "must be positive or zero")
                                          Long userId,
                                          @PathVariable
                                          @NotNull(message = "must not be null")
                                          @PositiveOrZero(message = "must be positive or zero")
                                          Long requestId) {
        log.trace("{}: cancel() call with userId: {}, requestId: {}", className, userId, requestId);

        CancelParticipationRequest cancelParticipationRequest = CancelParticipationRequest.builder()
                .userId(userId)
                .requestId(requestId)
                .build();
        return service.cancel(cancelParticipationRequest);
    }

    @Override
    @GetMapping("/users/{userId}/events/{eventId}/participation")
    public boolean isParticipantApproved(@PathVariable @PositiveOrZero @NotNull Long userId,
                                         @PathVariable @PositiveOrZero @NotNull Long eventId) {
        log.trace("{}: isParticipantApproved() call with userId: {}, eventId: {}",
                className, userId, eventId);
        return service.isParticipantApproved(userId, eventId);
    }
}
