package ru.yandex.practicum.user_service.service;

import ru.yandex.practicum.user_service.model.AdminUserFindParam;
import ru.yandex.practicum.user_service.model.NewUserRequest;
import ru.yandex.practicum.user_service.model.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> find(AdminUserFindParam param);

    UserDto create(NewUserRequest newUserRequest);

    void delete(Long userId);
}
