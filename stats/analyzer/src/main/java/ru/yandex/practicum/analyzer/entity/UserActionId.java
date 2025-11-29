package ru.yandex.practicum.analyzer.entity;

import java.io.Serializable;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserActionId implements Serializable {
    private Long userId;
    private Long eventId;
}
