package ru.yandex.practicum.request_service.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.core_api.exception.ConflictException;
import ru.yandex.practicum.core_api.exception.NotFoundException;
import ru.yandex.practicum.core_api.feign.EventServiceClient;
import ru.yandex.practicum.core_api.feign.UserServiceClient;
import ru.yandex.practicum.core_api.model.event.dto.*;
import ru.yandex.practicum.core_api.model.user.UserDto;
import ru.yandex.practicum.request_service.mapper.ParticipationRequestMapper;
import ru.yandex.practicum.core_api.model.request.CancelParticipationRequest;
import ru.yandex.practicum.core_api.model.request.NewParticipationRequest;
import ru.yandex.practicum.request_service.model.ParticipationRequest;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestStatus;
import ru.yandex.practicum.request_service.repository.ParticipationRequestRepository;
import ru.yandex.practicum.core_api.util.DataProvider;
import ru.yandex.practicum.core_api.util.ExistenceValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ParticipationRequestServiceImpl implements ParticipationRequestService,
        ExistenceValidator<ParticipationRequest>, DataProvider<ParticipationRequestDto, ParticipationRequest> {

    private final String className = this.getClass().getSimpleName();
    private final ParticipationRequestRepository participationRequestRepository;
    private final ParticipationRequestMapper participationRequestMapper;
    private final EventServiceClient eventServiceClient;
    private final UserServiceClient userServiceClient;


    @Override
    public List<ParticipationRequestDto> find(Long userId) {

        List<ParticipationRequestDto> result = participationRequestRepository.findAllByRequesterId(userId).stream()
                .map(this::getDto)
                .toList();
        log.info("{}: result of find(): {}", className, result);
        return result;
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(NewParticipationRequest newParticipationRequest) {
        Long requesterId = newParticipationRequest.getUserId();
        Long eventId = newParticipationRequest.getEventId();

        if (participationRequestRepository.existsByRequesterIdAndEventId(
                requesterId, eventId)) {
            log.info("{}: attempt to create already existent participationRequest with requesterId: {}, eventId: {}",
                    className, requesterId, eventId);
            throw new ConflictException("Duplicate request.", "participationRequest with requesterId: " + requesterId +
                    ", and eventId: " + eventId + " already exists");
        }

        EventFullDto event;
        UserDto user;

        try {
            event = eventServiceClient.getEventByIdInternal(eventId);
        } catch (FeignException.NotFound e) {
            throw new ConflictException("Event not found",
                    "Event with id=" + eventId + " was not found");
        }

        if (event.getInitiator() != null && event.getInitiator().equals(requesterId)) {
            log.info("{}: attempt to create participationRequest by an event initiator with requesterId: {}, eventId: {}, " +
                    "initiatorId: {}", className, requesterId, eventId, event.getInitiator());
            throw new ConflictException("Initiator can't create participation request.", "requesterId: "
                    + requesterId + " equals to initiatorId: " + event.getInitiator());
        }

        try {
            user = userServiceClient.getUserById(requesterId);
        } catch (FeignException.NotFound e) {
            throw new ConflictException("User not found",
                    "User with id=" + requesterId + " was not found");
        }

        if (event.getPublishedOn() == null) {
            log.info("{}: attempt to create participationRequest for not published event with " +
                    "requesterId: {}, eventId: {}", className, requesterId, eventId);
            throw new ConflictException("Can't create participation request for unpublished event.",
                    "event with id: " + eventId + " is not published yet");
        }

        // check participant limit on event
        if (event.getParticipantLimit() != 0) {
            List<ParticipationRequest> alreadyConfirmed = participationRequestRepository
                    .findAllByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
            int remainingSpots = event.getParticipantLimit() - alreadyConfirmed.size();
            if (remainingSpots <= 0) {
                log.info("{}: attempt to create participationRequest, but participantLimit: {} is reached",
                        className, event.getParticipantLimit());
                throw new ConflictException("Participant limit is reached.", "event with id: " + eventId +
                        " has participant limit of: " + event.getParticipantLimit());
            }
        }

        ParticipationRequest request = mapEntity(newParticipationRequest);

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
        }

        ParticipationRequestDto result = getDto(participationRequestRepository.save(request));
        log.info("{}: result of create():: {}", className, result);
        return result;
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(CancelParticipationRequest cancelParticipationRequest) {
        ParticipationRequest request = participationRequestRepository
                .findById(cancelParticipationRequest.getRequestId())
                .orElseThrow(() -> {
                    log.info("{}: attempt to find participationRequest with id: {}",
                            cancelParticipationRequest, cancelParticipationRequest.getRequestId());
                    return new NotFoundException("The required object was not found.",
                            "ParticipationRequest with id=" + cancelParticipationRequest.getRequestId() +
                                    " was not found");
                });
        if (!request.getRequesterId().equals(cancelParticipationRequest.getUserId())) {
            log.info("{}: attempt to cancel participationRequest by not an owner", className);
            throw new ConflictException("Request can be cancelled only by an owner",
                    "User with id=" + cancelParticipationRequest.getUserId() +
                            " is not an owner of request with id=" + cancelParticipationRequest.getRequestId());
        }

        ParticipationRequestDto result = participationRequestMapper.toDto(
                participationRequestRepository.findById(cancelParticipationRequest.getRequestId()).get());
        result.setStatus(ParticipationRequestStatus.CANCELED);
        participationRequestRepository.deleteById(cancelParticipationRequest.getRequestId());

        log.info("{}: result of cancel(): {}, which has been deleted", className, result);
        return result;
    }

    private ParticipationRequest mapEntity(NewParticipationRequest newParticipationRequest) {
        Long userId = newParticipationRequest.getUserId();
        Long eventId = newParticipationRequest.getEventId();

        ParticipationRequest entity = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .requesterId(userId)
                .eventId(eventId)
                .status(ParticipationRequestStatus.PENDING)
                .build();

        log.trace("{}: result of mapEntity(): {}", className, entity);
        return entity;
    }

    @Override
    public ParticipationRequestDto getDto(ParticipationRequest entity) {
        return participationRequestMapper.toDto(entity);
    }

    @Override
    public void validateExists(Long id) {
        if (participationRequestRepository.findById(id).isEmpty()) {
            log.info("{}: attempt to find participationRequest with id: {}", className, id);
            throw new NotFoundException("The required object was not found.",
                    "ParticipationRequest with id=" + id + " was not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isParticipantApproved(Long userId, Long eventId) {
        return participationRequestRepository
                .existsByRequesterIdAndEventIdAndStatus(
                        userId,
                        eventId,
                        ParticipationRequestStatus.CONFIRMED
                );
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestStatus(long userId, long eventId,
                                                                   EventRequestStatusUpdateRequest updateRequest) {
        EventFullDto event = eventServiceClient.getEventByIdInternal(eventId);
        List<ParticipationRequest> requestsByEventId = participationRequestRepository.findAllById(updateRequest.getRequestIds());

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return new EventRequestStatusUpdateResult(
                    requestsByEventId.stream().map(participationRequestMapper::toDto).toList(), List.of());
        }

        List<ParticipationRequest> alreadyConfirmed = participationRequestRepository
                .findAllByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
        AtomicInteger remainingSpots = new AtomicInteger(event.getParticipantLimit() - alreadyConfirmed.size());

        if (remainingSpots.get() <= 0) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }

        requestsByEventId.forEach(request -> {
            if (request.getStatus() != ParticipationRequestStatus.PENDING) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "It's not allowed to change the status of the request");
            }
        });

        List<ParticipationRequestDto> confirmedDto = new ArrayList<>();
        List<ParticipationRequestDto> rejectedDto = new ArrayList<>();

        requestsByEventId.forEach(request -> {
            if (remainingSpots.get() > 0 && updateRequest.getStatus() == StatusUpdateRequest.CONFIRMED) {
                request.setStatus(ParticipationRequestStatus.CONFIRMED);
                confirmedDto.add(participationRequestMapper.toDto(request));
                remainingSpots.getAndDecrement();
            } else {
                request.setStatus(ParticipationRequestStatus.REJECTED);
                rejectedDto.add(participationRequestMapper.toDto(request));
            }
        });

        if (!confirmedDto.isEmpty()) {
            participationRequestRepository.updateStatus(
                    confirmedDto.stream().map(ParticipationRequestDto::getId).toList(),
                    ParticipationRequestStatus.CONFIRMED);
        }
        if (!rejectedDto.isEmpty()) {
            participationRequestRepository.updateStatus(
                    rejectedDto.stream().map(ParticipationRequestDto::getId).toList(),
                    ParticipationRequestStatus.REJECTED);
        }
        if (remainingSpots.get() == 0) {
            List<Long> pendingIds = participationRequestRepository
                    .findAllByEventIdAndStatus(eventId, ParticipationRequestStatus.PENDING)
                    .stream().map(ParticipationRequest::getId).toList();
            if (!pendingIds.isEmpty()) {
                participationRequestRepository.updateStatus(pendingIds, ParticipationRequestStatus.REJECTED);
            }
        }
        return new EventRequestStatusUpdateResult(confirmedDto, rejectedDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventParticipationRequestsByUser(long userId, long eventId) {


        List<ParticipationRequest> requestsByEventId = participationRequestRepository.findAllByEventId(eventId);
        return requestsByEventId.stream().map(participationRequestMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventRequestCount> countGroupByEventId(List<Long> eventIds) {
        log.trace("{}: countGroupByEventId() call with eventIds: {}", className, eventIds);

        if (eventIds == null || eventIds.isEmpty()) {
            log.info("{}: empty eventIds list provided", className);
            return List.of();
        }

        List<EventRequestCount> result = participationRequestRepository.countConfirmedRequestsByEventIdIn(eventIds);
        log.info("{}: result of countGroupByEventId(): {}", className, result);
        return result;
    }
}
