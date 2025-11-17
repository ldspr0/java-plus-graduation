package ru.yandex.practicum.explore.with.me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.core_api.model.compilation.Compilation;
import ru.yandex.practicum.core_api.model.compilation.CompilationCreateDto;
import ru.yandex.practicum.core_api.model.compilation.CompilationRequestDto;
import ru.yandex.practicum.core_api.model.event.Event;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "events")
    Compilation toEntity(CompilationCreateDto compilationCreateDto, List<Event> events);

    @Mapping(target = "events", source = "events")
    CompilationRequestDto toRequestDto(Compilation compilation);
}
