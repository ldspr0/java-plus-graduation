package ru.yandex.practicum.core_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.core_api.interfaces.CompilationInterface;

@FeignClient(name = "compilation-service")
public interface CompilationServiceClient extends CompilationInterface {
}