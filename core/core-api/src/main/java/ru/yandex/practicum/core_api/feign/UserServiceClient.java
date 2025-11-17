package ru.yandex.practicum.core_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.core_api.interfaces.UserInterface;

@FeignClient(name = "user-service")
public interface UserServiceClient extends UserInterface {
}