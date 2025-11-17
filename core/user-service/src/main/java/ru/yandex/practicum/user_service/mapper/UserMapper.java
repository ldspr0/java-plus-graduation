package ru.yandex.practicum.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.user_service.model.NewUserRequest;
import ru.yandex.practicum.user_service.model.User;
import ru.yandex.practicum.user_service.model.UserDto;
import ru.yandex.practicum.user_service.model.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User entity);

    UserShortDto toShortDto(User entity);

    @Mapping(target = "id", ignore = true)
    User toEntity(NewUserRequest newUserRequest);
}