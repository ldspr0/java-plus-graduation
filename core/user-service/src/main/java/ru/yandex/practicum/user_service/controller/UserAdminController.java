package ru.yandex.practicum.user_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.core_api.interfaces.UserInterface;
import ru.yandex.practicum.core_api.model.user.AdminUserFindParam;
import ru.yandex.practicum.core_api.model.user.NewUserRequest;
import ru.yandex.practicum.core_api.model.user.UserDto;
import ru.yandex.practicum.user_service.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class UserAdminController implements UserInterface {
    private final UserService service;
    private final String controllerName = this.getClass().getSimpleName();

    @Override
    @GetMapping("/admin/users")
    public List<UserDto> find(@RequestParam(required = false)
                              List<Long> ids,
                              @RequestParam(defaultValue = "0")
                              @PositiveOrZero(message = "must be positive or zero")
                              int from,
                              @RequestParam(defaultValue = "10")
                              @Positive(message = "must be positive")
                              int size,
                              HttpServletRequest request) {
        log.trace("{}: find() call with ids: {}, from: {}, size: {}", controllerName, ids, from, size);

        AdminUserFindParam param = AdminUserFindParam.builder()
                .ids(ids)
                .from(from)
                .size(size)
                .build();
        return service.find(param);
    }

    @Override
    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody
                          @Valid
                          NewUserRequest newUserRequest,
                          HttpServletRequest request) {
        log.trace("{}: create() call with newUserRequest: {}", controllerName, newUserRequest);
        return service.create(newUserRequest);
    }

    @Override
    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable
                       @Positive(message = "must be positive")
                       Long userId,
                       HttpServletRequest request) {
        log.trace("{}: delete() call with userId: {}", controllerName, userId);
        service.delete(userId);
    }

    @Override
    @GetMapping("/admin/user/{userId}")
    public UserDto getUserById(@PathVariable
                        @Positive(message = "must be positive")
                        Long userId) {
        log.trace("{}: getUserById() call with userId: {}", controllerName, userId);
        return service.getUserById(userId);
    }

}
