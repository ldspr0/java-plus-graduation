package ru.yandex.practicum.explore.with.me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.explore.with.me.model.Comment;
import ru.yandex.practicum.core_api.model.comment.CommentDto;
import ru.yandex.practicum.core_api.model.comment.CommentUpdateDto;
import ru.yandex.practicum.core_api.model.comment.CommentUserDto;
import ru.yandex.practicum.core_api.model.comment.CreateUpdateCommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "authorId", source = "authorId")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    Comment toModel(CreateUpdateCommentDto createUpdateCommentDto);

    @Mapping(target = "eventDto", source = "event")
    CommentUserDto toUserDto(Comment comment);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "authorId", source = "authorId")
    CommentUpdateDto toUpdateDto(Comment comment);
}
