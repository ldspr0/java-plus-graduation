package ru.yandex.practicum.explore.with.me.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.explore.with.me.model.Category;
import ru.yandex.practicum.core_api.model.category.CategoryDto;
import ru.yandex.practicum.core_api.model.category.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Category toModel(NewCategoryDto newCategoryDto);

    CategoryDto toDto(Category category);

    @Mapping(target = "events", ignore = true)
    Category toEntity(CategoryDto categoryDto);
}