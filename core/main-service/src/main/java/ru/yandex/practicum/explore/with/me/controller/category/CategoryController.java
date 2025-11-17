package ru.yandex.practicum.explore.with.me.controller.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.interfaces.CategoryInterface;
import ru.yandex.practicum.core_api.model.category.CategoryDto;
import ru.yandex.practicum.core_api.model.category.NewCategoryDto;
import ru.yandex.practicum.explore.with.me.service.category.CategoryServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryController implements CategoryInterface {
    private final String className = this.getClass().getSimpleName();
    private final CategoryServiceImpl categoryService;

    @Override
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto category) {
        log.trace("{}: createCategory() call with category: {}", className, category);
        return categoryService.createCategory(category);
    }

    @Override
    @DeleteMapping("/admin/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull @PositiveOrZero Long id) {
        log.trace("{}:  deleteCategory() call with id: {}", className, id);
        categoryService.deleteCategory(id);
    }

    @Override
    @PatchMapping("/admin/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable @NotNull @PositiveOrZero Long id,
                                      @RequestBody @Valid NewCategoryDto category) {
        log.trace("{}: updateCategory with id: {}", className, id);
        return categoryService.updateCategory(id, category);
    }

    @Override
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.trace("{}: getCategories() call with from: {}, size: {}", className, from, size);
        return categoryService.getCategories(from, size);
    }

    @Override
    @GetMapping("/categories/{id}")
    public CategoryDto getCategoryById(@PathVariable("id") @PositiveOrZero @NotNull Long id) {
        log.trace("{}: getCategoryById() with id: {}", className, id);
        return categoryService.getCategory(id);
    }
}
