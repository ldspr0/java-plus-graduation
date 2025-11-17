package ru.yandex.practicum.request_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.core_api.model.request.ParticipationRequest;
import ru.yandex.practicum.core_api.model.request.ParticipationRequestDto;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toDto(ParticipationRequest entity);
}
