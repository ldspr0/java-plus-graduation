package ru.yandex.practicum.core_api.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.model.category.CategoryDto;
import ru.yandex.practicum.core_api.model.category.NewCategoryDto;

import java.util.List;

public interface CategoryInterface {

    // Admin endpoints
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto createCategory(@RequestBody @Valid NewCategoryDto category);

    @DeleteMapping("/admin/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable("id") @NotNull @PositiveOrZero Long id);

    @PatchMapping("/admin/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    CategoryDto updateCategory(@PathVariable("id") @NotNull @PositiveOrZero Long id,
                               @RequestBody @Valid NewCategoryDto category);

    // Public endpoints
    @GetMapping("/categories")
    List<CategoryDto> getCategories(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(value = "size", defaultValue = "10") @Positive int size);

    @GetMapping("/categories/{id}")
    CategoryDto getCategoryById(@PathVariable("id") @PositiveOrZero @NotNull Long id);
}