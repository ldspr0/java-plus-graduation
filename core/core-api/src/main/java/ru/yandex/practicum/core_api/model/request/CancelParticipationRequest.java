package ru.yandex.practicum.core_api.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CancelParticipationRequest {
    private Long userId;
    private Long requestId;
}
