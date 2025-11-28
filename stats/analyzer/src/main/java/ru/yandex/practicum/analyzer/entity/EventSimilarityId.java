package ru.yandex.practicum.analyzer.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventSimilarityId implements Serializable {
    private Long eventA;
    private Long eventB;
}
