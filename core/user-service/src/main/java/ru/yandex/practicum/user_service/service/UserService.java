package ru.yandex.practicum.user_service.service;

import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.core_api.model.user.AdminUserFindParam;
import ru.yandex.practicum.core_api.model.user.NewUserRequest;
import ru.yandex.practicum.core_api.model.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> find(AdminUserFindParam param);

    UserDto create(NewUserRequest newUserRequest);

    void delete(Long userId);

    UserDto getUserById(Long userId);
}
