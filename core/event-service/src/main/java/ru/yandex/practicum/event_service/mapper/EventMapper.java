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

    @Mapping(target = "initiator", source = "initiatorId")
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "rating", ignore = true) // Игнорировать rating в базовом методе
    EventFullDto toFullDto(Event event);

    @Mapping(target = "initiator", source = "initiatorId")
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "rating", ignore = true) // Игнорировать rating в базовом методе
    EventShortDto toShortDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "initiatorId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Event toModel(NewEventDto eventDto);

    @Mapping(target = "initiatorId", source = "initiator")
    @Mapping(target = "comments", ignore = true)
    Event toEntity(EventFullDto eventFullDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    void updateFromAdmin(UpdateEventAdminRequestDto dto, @MappingTarget Event event);

    // Оригинальные методы (оставить для обратной совместимости)
    default EventFullDto toFullDtoWithStats(Event event, EventStatistics stats) {
        EventFullDto dto = toFullDto(event);
        dto.setConfirmedRequests(stats.getConfirmedRequests(event.getId()));
        return dto;
    }

    default EventShortDto toShortDtoWithStats(Event event, EventStatistics stats) {
        EventShortDto dto = toShortDto(event);
        dto.setConfirmedRequests(stats.getConfirmedRequests(event.getId()));
        return dto;
    }

    // НОВЫЕ методы с рейтингом
    default EventFullDto toFullDtoWithStatsAndRating(Event event, EventStatistics stats, Double rating) {
        EventFullDto dto = toFullDtoWithStats(event, stats);
        dto.setRating(rating); // Устанавливаем рейтинг
        return dto;
    }

    default EventShortDto toShortDtoWithStatsAndRating(Event event, EventStatistics stats, Double rating) {
        EventShortDto dto = toShortDtoWithStats(event, stats);
        dto.setRating(rating); // Устанавливаем рейтинг
        return dto;
    }

    // Дополнительные методы если нужно разделить логику
    default EventFullDto toFullDtoWithRating(Event event, Double rating) {
        EventFullDto dto = toFullDto(event);
        dto.setRating(rating);
        return dto;
    }

    default EventShortDto toShortDtoWithRating(Event event, Double rating) {
        EventShortDto dto = toShortDto(event);
        dto.setRating(rating);
        return dto;
    }
}