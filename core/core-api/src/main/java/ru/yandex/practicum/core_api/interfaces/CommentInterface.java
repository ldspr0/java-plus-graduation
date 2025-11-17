package ru.yandex.practicum.core_api.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.model.comment.CommentDto;
import ru.yandex.practicum.core_api.model.comment.CommentUpdateDto;
import ru.yandex.practicum.core_api.model.comment.CommentUserDto;
import ru.yandex.practicum.core_api.model.comment.CreateUpdateCommentDto;

import java.util.List;

public interface CommentInterface {

    // Admin endpoints
    @GetMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    CommentDto getCommentById(@PathVariable("commentId") @NotNull @PositiveOrZero Long commentId);

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCommentById(@PathVariable("commentId") @NotNull @PositiveOrZero Long commentId);

    // Public endpoints
    @PostMapping("/users/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto createComment(@PathVariable("userId") @NotNull @PositiveOrZero Long userId,
                             @RequestParam("eventId") @NotNull @PositiveOrZero Long eventId,
                             @RequestBody @Valid CreateUpdateCommentDto commentDto);

    @PatchMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    CommentUpdateDto updateComment(@PathVariable("userId") @NotNull @PositiveOrZero Long userId,
                                   @PathVariable("commentId") @NotNull @PositiveOrZero Long commentId,
                                   @RequestBody @Valid CreateUpdateCommentDto commentDto);

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable("userId") @NotNull @PositiveOrZero Long userId,
                       @PathVariable("commentId") @NotNull @PositiveOrZero Long commentId);

    @GetMapping("/users/{userId}/comments")
    @ResponseStatus(HttpStatus.OK)
    List<CommentUserDto> getCommentsByUser(@PathVariable("userId") @NotNull @PositiveOrZero Long userId,
                                           @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(value = "size", defaultValue = "10") @Positive int size);

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    List<CommentDto> getCommentsByEvent(@PathVariable("eventId") @PositiveOrZero @NotNull Long eventId,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size);
}