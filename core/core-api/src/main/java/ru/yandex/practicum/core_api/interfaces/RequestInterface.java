package ru.yandex.practicum.core_api.interfaces;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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


}
