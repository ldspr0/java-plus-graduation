package ru.yandex.practicum.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.core_api.model.user.NewUserRequest;
import ru.yandex.practicum.user_service.model.User;
import ru.yandex.practicum.core_api.model.user.UserDto;
import ru.yandex.practicum.core_api.model.user.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User entity);

    UserShortDto toShortDto(User entity);

    @Mapping(target = "id", ignore = true)
    User toEntity(NewUserRequest newUserRequest);

    User toEntity(UserDto userDto);
}