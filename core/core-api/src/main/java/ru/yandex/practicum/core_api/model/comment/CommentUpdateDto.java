package ru.yandex.practicum.core_api.model.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentUpdateDto {
    private long id;
    private String text;
    private Long authorId;
    private Long eventId;
    private LocalDateTime updatedOn;

}
