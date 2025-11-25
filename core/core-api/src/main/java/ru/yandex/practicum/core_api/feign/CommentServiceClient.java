package ru.yandex.practicum.core_api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.core_api.interfaces.CommentInterface;

@FeignClient(name = "comment-service")
public interface CommentServiceClient extends CommentInterface {
}
