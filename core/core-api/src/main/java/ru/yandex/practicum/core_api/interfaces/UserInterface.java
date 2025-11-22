package ru.yandex.practicum.core_api.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.model.user.NewUserRequest;
import ru.yandex.practicum.core_api.model.user.UserDto;

import java.util.List;

public interface UserInterface {

    @GetMapping("/admin/user/{userId}")
    UserDto getUserById(@PathVariable
                        @Positive(message = "must be positive")
                        Long userId);

    @GetMapping("/admin/users")
    List<UserDto> find(@RequestParam(value = "ids", required = false) List<Long> ids,
                       @RequestParam(value = "from", defaultValue = "0")
                       @PositiveOrZero(message = "must be positive or zero") int from,
                       @RequestParam(value = "size", defaultValue = "10")
                       @Positive(message = "must be positive") int size,
                       HttpServletRequest request);

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@RequestBody @Valid NewUserRequest newUserRequest,
                   HttpServletRequest request);

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("userId") @Positive(message = "must be positive") Long userId,
                HttpServletRequest request);
}