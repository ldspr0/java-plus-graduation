package ru.yandex.practicum.core_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.core_api.interfaces.RequestInterface;

@FeignClient(name = "request-service")
public interface RequestServiceClient extends RequestInterface {
}