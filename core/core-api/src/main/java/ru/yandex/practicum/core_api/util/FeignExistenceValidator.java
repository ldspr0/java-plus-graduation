package ru.yandex.practicum.core_api.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.core_api.exception.NotFoundException;
import ru.yandex.practicum.core_api.feign.*;
import ru.yandex.practicum.core_api.mapper.CategoryMapper;
import ru.yandex.practicum.core_api.mapper.EventMapper;
import ru.yandex.practicum.core_api.mapper.UserMapper;
import ru.yandex.practicum.core_api.model.category.Category;
import ru.yandex.practicum.core_api.model.category.CategoryDto;
import ru.yandex.practicum.core_api.model.event.Event;
import ru.yandex.practicum.core_api.model.event.dto.EventFullDto;
import ru.yandex.practicum.core_api.model.user.User;
import ru.yandex.practicum.core_api.model.user.UserDto;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeignExistenceValidator {

    private final UserServiceClient userServiceClient;
    private final EventServiceClient eventServiceClient;
    @Getter
    private final RequestServiceClient requestServiceClient;
    private final CategoryServiceClient categoryServiceClient;

    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;

    public void validateUserExists(Long userId) {
        try {
            List<UserDto> users = userServiceClient.find(List.of(userId), 0, 1, null);
            if (users.isEmpty()) {
                throw new NotFoundException("User not found", "User with id=" + userId + " was not found");
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error validating user existence: {}", e.getMessage());
            throw new NotFoundException("User not found", "User with id=" + userId + " was not found");
        }
    }

    public UserDto getUserDtoById(Long userId) {
        try {
            List<UserDto> users = userServiceClient.find(List.of(userId), 0, 1, null);
            if (users.isEmpty()) {
                throw new NotFoundException("User not found", "User with id=" + userId + " was not found");
            }
            return users.get(0);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting user by id: {}", e.getMessage());
            throw new NotFoundException("User not found", "User with id=" + userId + " was not found");
        }
    }

    public User getUserById(Long userId) {
        UserDto userDto = getUserDtoById(userId);
        return userMapper.toEntity(userDto);
    }

    public void validateEventExists(Long eventId) {
        try {
            eventServiceClient.getEventById(eventId, null);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error validating event existence: {}", e.getMessage());
            throw new NotFoundException("Event not found", "Event with id=" + eventId + " was not found");
        }
    }

    public EventFullDto getEventDtoById(Long eventId) {
        try {
            return eventServiceClient.getEventById(eventId, null);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting event by id: {}", e.getMessage());
            throw new NotFoundException("Event not found", "Event with id=" + eventId + " was not found");
        }
    }

    public Event getEventById(Long eventId) {
        EventFullDto eventDto = getEventDtoById(eventId);
        return eventMapper.toEntity(eventDto);
    }

    public boolean isParticipantApproved(Long userId, Long eventId) {
        try {
            return requestServiceClient.isParticipantApproved(userId, eventId);
        } catch (Exception e) {
            log.error("Error checking participant approval: {}", e.getMessage());
            return false;
        }
    }

    public void validateCategoryExists(Long categoryId) {
        try {
            categoryServiceClient.getCategoryById(categoryId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error validating category existence: {}", e.getMessage());
            throw new NotFoundException("Category not found", "Category with id=" + categoryId + " was not found");
        }
    }

    public CategoryDto getCategoryDtoById(Long categoryId) {
        try {
            return categoryServiceClient.getCategoryById(categoryId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting category by id: {}", e.getMessage());
            throw new NotFoundException("Category not found", "Category with id=" + categoryId + " was not found");
        }
    }

    public Category getCategoryById(Long categoryId) {
        CategoryDto categoryDto = getCategoryDtoById(categoryId);
        return categoryMapper.toEntity(categoryDto);
    }
}