package ru.yandex.practicum.event_service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.yandex.practicum.core_api.model.event.EventStatistics;
import ru.yandex.practicum.core_api.model.event.dto.EventFullDto;
import ru.yandex.practicum.core_api.model.event.dto.EventShortDto;
import ru.yandex.practicum.core_api.model.event.dto.NewEventDto;
import ru.yandex.practicum.core_api.model.event.dto.UpdateEventAdminRequestDto;
import ru.yandex.practicum.event_service.model.Event;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, CommentMapper.class})
public interface EventMapper {
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    EventFullDto toFullDto(Event event);

    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    EventShortDto toShortDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "initiatorId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Event toModel(NewEventDto eventDto);

    @Mapping(target = "comments", ignore = true)
    Event toEntity(EventFullDto eventFullDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    void updateFromAdmin(UpdateEventAdminRequestDto dto, @MappingTarget Event event);

    default EventFullDto toFullDtoWithStats(Event event, EventStatistics stats) {
        EventFullDto dto = toFullDto(event);
        dto.setViews(stats.getViews(event.getId()));
        dto.setConfirmedRequests(stats.getConfirmedRequests(event.getId()));
        return dto;
    }

    default EventShortDto toShortDtoWithStats(Event event, EventStatistics stats) {
        EventShortDto dto = toShortDto(event);
        dto.setViews(stats.getViews(event.getId()));
        dto.setConfirmedRequests(stats.getConfirmedRequests(event.getId()));
        return dto;
    }
}
