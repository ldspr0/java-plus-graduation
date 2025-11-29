package ru.yandex.practicum.analyzer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events_similarity")
@IdClass(EventSimilarityId.class)
public class EventSimilarity {

    @Id
    private Long eventA;

    @Id
    private Long eventB;


    private Double score;
    private Instant timestamp;
}