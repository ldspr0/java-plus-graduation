package ru.yandex.practicum.event_service.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.core_api.exception.BadRequestException;
import ru.yandex.practicum.core_api.exception.NotFoundException;
import ru.yandex.practicum.event_service.mapper.CommentMapper;
import ru.yandex.practicum.event_service.model.Comment;
import ru.yandex.practicum.core_api.model.comment.CommentDto;
import ru.yandex.practicum.core_api.model.comment.CommentUpdateDto;
import ru.yandex.practicum.core_api.model.comment.CommentUserDto;
import ru.yandex.practicum.core_api.model.comment.CreateUpdateCommentDto;
import ru.yandex.practicum.event_service.repository.CommentRepository;
import ru.yandex.practicum.core_api.util.ExistenceValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class CommentServiceImpl implements CommentService, ExistenceValidator<Comment> {

    private static final String OBJECT_NOT_FOUND = "Required object was not found.";
    private static final String CONDITIONS_NOT_MET = "Conditions are not met.";
    private final String className = this.getClass().getSimpleName();

    private final CommentRepository commentRepository;
//    private final FeignExistenceValidator feignExistenceValidator;
    private final CommentMapper mapper;

    // admin

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentDto getCommentById(Long id) {
        CommentDto result = mapper.toDto(getOrThrow(id));
        log.info("{}: result of getCommentById({}): {}", className, id, result);
        return result;
    }

    @Override
    public void deleteCommentByAdmin(Long id) {
        log.info("{}: comment with id: {} was deleted, if it existed", className, id);
        commentRepository.deleteById(id);
    }

    //private

    @Override
    public CommentDto createComment(Long userId, Long eventId, CreateUpdateCommentDto dto) {
        validateText(dto.getText(), 100);

        // Проверяем существование пользователя и события через Feign
//        feignExistenceValidator.validateUserExists(userId);
//        feignExistenceValidator.validateEventExists(eventId);

        // Получаем пользователя и событие как entity
//        User author = feignExistenceValidator.getUserById(userId);
//        Event event = feignExistenceValidator.getEventById(eventId);

        // Проверяем, что событие уже прошло
//        if (event.getEventDate().isAfter(LocalDateTime.now())) {
//            log.info("CommentServiceImpl: attempt to comment event, which has not been happened yet");
//            throw new ConflictException(CONDITIONS_NOT_MET, "Only past events can be commented on");
//        }
//
//        // Проверяем, что пользователь участвовал в событии через Feign
//        if (!feignExistenceValidator.isParticipantApproved(userId, eventId)) {
//            log.info("{}: attempt to comment on event with id: {}, " +
//                    "in which user with id: {} did not participate", className, eventId, userId);
//            throw new ConflictException(CONDITIONS_NOT_MET, "Only events the user participated in can be commented on");
//        }
//
//        Comment comment = mapper.toModel(dto);
//        comment.setAuthor(author);
//        comment.setEvent(event);
//
//        CommentDto result = mapper.toDto(commentRepository.save(comment));
//        log.info("{}: result of createComment(): {}", className, result);
//        return result;
        return null;
    }

    @Override
    public CommentUpdateDto updateComment(Long userId, Long commentId, CreateUpdateCommentDto dto) {
        // Проверяем существование пользователя через Feign
//        feignExistenceValidator.validateUserExists(userId);
//        validateText(dto.getText(), 1000);
//
//        Comment comment = getOrThrow(commentId);
//        if (!comment.getAuthor().getId().equals(userId)) {
//            log.info("{}: attempt to redact comment with id: {} by a user with id: {}, " +
//                    "which is not an author", className, commentId, userId);
//            throw new ForbiddenException(CONDITIONS_NOT_MET,
//                    "Only author can redact comment");
//        }
//
//        comment.setText(dto.getText());
//        comment.setUpdatedOn(LocalDateTime.now());
//        CommentUpdateDto result = mapper.toUpdateDto(comment);
//        log.info("{}: result of updateComment(): {}", className, result);
//        return result;
        return null;
    }

    @Override
    public void deleteCommentByAuthor(Long userId, Long commentId) {
//        // Проверяем существование пользователя через Feign
//        feignExistenceValidator.validateUserExists(userId);
//        Comment comment = getOrThrow(commentId);
//
//        if (!comment.getAuthor().getId().equals(userId)) {
//            log.info("{}: attempt to delete comment, but user with id: {} " +
//                    "is not an author", className, userId);
//            throw new ForbiddenException(CONDITIONS_NOT_MET,
//                    "Only author / admin can delete comment");
//        }
//
//        commentRepository.delete(comment);
//        log.info("{}: comment with id: {} was deleted", className, commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentUserDto> getCommentsByAuthor(Long userId, Pageable pageable) {
//        // Проверяем существование пользователя через Feign
//        feignExistenceValidator.validateUserExists(userId);
//
//        List<CommentUserDto> result = commentRepository.findByAuthorIdOrderByCreatedOnDesc(userId, pageable)
//                .stream()
//                .map(mapper::toUserDto)
//                .toList();
//        log.info("{}: result of getCommentsByAuthor(): {}", className, result);
//        return result;
        return null;
    }

    //public

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByEvent(Long eventId, Pageable pageable) {
//        // Проверяем существование события через Feign
//        feignExistenceValidator.validateEventExists(eventId);
//
//        List<CommentDto> result = commentRepository.findByEventIdOrderByCreatedOnDesc(eventId, pageable)
//                .stream()
//                .map(mapper::toDto)
//                .toList();
//        log.info("{}: result of getCommentsByEvent(): {}", className, result);
//        return result;
        return null;
    }

    private void validateText(String text, int max) {
        if (text == null || text.isBlank() || text.length() > max) {
            throw new BadRequestException("Text param constraint violation.",
                    String.format("Comment text has to be 1–%d symbols", max));
        }
    }

    private Comment getOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                            log.info("{}: comment with id: {} was not found", className, id);
                            return new NotFoundException(
                                    OBJECT_NOT_FOUND,
                                    String.format("Comment with id: %d was not found", id));
                        }
                );
    }

    @Override
    public void validateExists(Long id) {
        if (commentRepository.findById(id).isEmpty()) {
            log.info("{}: attempt to find comment with id: {}", className, id);
            throw new NotFoundException(OBJECT_NOT_FOUND,
                    "Comment with id=" + id + " was not found");
        }
    }
}