package ru.yandex.practicum.core_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.core_api.interfaces.CategoryInterface;

@FeignClient(name = "category-service")
public interface CategoryServiceClient extends CategoryInterface {
}
