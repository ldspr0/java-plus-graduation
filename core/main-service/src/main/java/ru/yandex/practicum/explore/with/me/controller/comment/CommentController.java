package ru.yandex.practicum.explore.with.me.controller.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.interfaces.CommentInterface;
import ru.yandex.practicum.core_api.model.comment.CommentDto;
import ru.yandex.practicum.core_api.model.comment.CommentUpdateDto;
import ru.yandex.practicum.core_api.model.comment.CommentUserDto;
import ru.yandex.practicum.core_api.model.comment.CreateUpdateCommentDto;
import ru.yandex.practicum.explore.with.me.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CommentController implements CommentInterface {
    private final String className = this.getClass().getSimpleName();
    private final CommentService commentService;

    @Override
    @GetMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable @NotNull @PositiveOrZero Long commentId) {
        log.trace("{}: getCommentById() call with commentId: {}", className, commentId);
        return commentService.getCommentById(commentId);
    }

    @Override
    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable @NotNull @PositiveOrZero Long commentId) {
        log.trace("{}: deleteCommentById() call with commentId: {}", className, commentId);
        commentService.deleteCommentByAdmin(commentId);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable @NotNull @PositiveOrZero Long userId,
                                    @RequestParam @NotNull @PositiveOrZero Long eventId,
                                    @RequestBody @Valid CreateUpdateCommentDto commentDto) {
        log.trace("{}: createComment() call with userId: {}, eventId: {}, commentDto: {}",
                className, userId, eventId, commentDto);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @Override
    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentUpdateDto updateComment(@PathVariable @NotNull @PositiveOrZero Long userId,
                                          @PathVariable @NotNull @PositiveOrZero Long commentId,
                                          @RequestBody @Valid CreateUpdateCommentDto commentDto) {
        log.info("Update comment {} for event {} by user {}", commentDto, commentId, userId);
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @Override
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @NotNull @PositiveOrZero Long userId,
                              @PathVariable @NotNull @PositiveOrZero Long commentId) {
        log.trace("{}:  deleteComment() call with userId: {}, commentId: {}", className, userId, commentId);
        commentService.deleteCommentByAuthor(userId, commentId);
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentUserDto> getCommentsByUser(@PathVariable @NotNull @PositiveOrZero Long userId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.trace("{}: getCommentsByUser() call with userId: {}, from: {}, size: {}", className, userId, from, size);
        return commentService.getCommentsByAuthor(
                userId,
                PageRequest.of(from / size, size)
        );
    }

    @Override
    @GetMapping("events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByEvent(@PathVariable @PositiveOrZero @NotNull Long eventId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.trace("{}: getCommentsByEvent() call with eventId: {}, from: {}, size: {}",
                className, eventId, from, size);
        return commentService.getCommentsByEvent(eventId, PageRequest.of(from / size, size));
    }
}
