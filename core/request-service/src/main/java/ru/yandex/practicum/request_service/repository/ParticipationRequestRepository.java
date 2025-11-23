package ru.yandex.practicum.request_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.core_api.model.event.dto.EventRequestCount;
import ru.yandex.practicum.request_service.model.ParticipationRequest;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestStatus;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);


    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    boolean existsByRequesterIdAndEventIdAndStatus(Long requesterId,
                                                   Long eventId,
                                                   ParticipationRequestStatus status);

    int countByEventId(Long eventId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, ParticipationRequestStatus status);

    @Modifying
    @Query("""
                UPDATE ParticipationRequest pr
                SET pr.status = :status
                WHERE pr.id IN :requestIds
            """)
    void updateStatus(@Param("requestIds") List<Long> requestIds, @Param("status") ParticipationRequestStatus status);

    @Query("SELECT pr FROM ParticipationRequest pr WHERE pr.eventId = :eventId AND pr.requesterId = :userId")
    List<ParticipationRequest> findParticipationRequestsByEventId(@Param("userId") long userId,
                                                                  @Param("eventId") long eventId);

    @Query("""
        SELECT new ru.yandex.practicum.core_api.model.event.dto.EventRequestCount(
            pr.eventId, 
            COUNT(pr.id)
        ) 
        FROM ParticipationRequest pr 
        WHERE pr.eventId IN :eventIds 
        AND pr.status = 'CONFIRMED' 
        GROUP BY pr.eventId
    """)
    List<EventRequestCount> countConfirmedRequestsByEventIdIn(@Param("eventIds") List<Long> eventIds);

}
