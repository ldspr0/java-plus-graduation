package ru.yandex.practicum.core_api.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.core_api.model.category.CategoryDto;
import ru.yandex.practicum.core_api.model.comment.CommentDto;
import ru.yandex.practicum.core_api.model.event.EventState;
import ru.yandex.practicum.core_api.model.event.Location;
import ru.yandex.practicum.user_service.model.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private List<CommentDto> comments;
    private int confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private long id;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}
