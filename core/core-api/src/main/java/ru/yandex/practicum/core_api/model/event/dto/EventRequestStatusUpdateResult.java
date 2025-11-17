package ru.yandex.practicum.core_api.model.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.request_service.model.ParticipationRequestDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
