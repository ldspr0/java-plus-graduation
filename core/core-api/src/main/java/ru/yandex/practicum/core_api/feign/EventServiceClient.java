package ru.yandex.practicum.core_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.core_api.interfaces.EventInterface;

@FeignClient(name = "event-service")
public interface EventServiceClient extends EventInterface {
}