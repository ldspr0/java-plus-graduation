package ru.yandex.practicum.request_service.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.request_service.model.ParticipationRequest;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    ParticipationRequestDto toDto(ParticipationRequest entity);
}
