package ru.yandex.practicum.request_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.core_api.exception.ConflictException;
import ru.yandex.practicum.core_api.exception.NotFoundException;
import ru.yandex.practicum.core_api.feign.EventServiceClient;
import ru.yandex.practicum.core_api.model.event.dto.EventFullDto;
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
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ParticipationRequestServiceImpl implements ParticipationRequestService,
        ExistenceValidator<ParticipationRequest>, DataProvider<ParticipationRequestDto, ParticipationRequest> {

    private final String className = this.getClass().getSimpleName();
    private final ParticipationRequestRepository participationRequestRepository;
    //    private final FeignExistenceValidator feignExistenceValidator;
    private final ParticipationRequestMapper participationRequestMapper;
    private final EventServiceClient eventServiceClient;


    @Override
    public List<ParticipationRequestDto> find(Long userId) {
//        feignExistenceValidator.validateUserExists(userId);

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

        EventFullDto event = eventServiceClient.getEventById(eventId, null);

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

        if (!event.isRequestModeration()) {
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
        //feignExistenceValidator.validateUserExists(cancelParticipationRequest.getUserId());
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
}
