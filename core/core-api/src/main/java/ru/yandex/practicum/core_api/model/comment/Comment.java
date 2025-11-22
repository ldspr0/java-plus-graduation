package ru.yandex.practicum.core_api.model.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.core_api.model.event.Event;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text", length = 1000)
    private String text;

    @Column(name = "created")
    private LocalDateTime createdOn;

    @Column(name = "updated")
    private LocalDateTime updatedOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

    @JoinColumn(name = "author_id")
    private Long authorId;
}
