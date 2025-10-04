package com.dungnb.be.mapper;

import com.dungnb.be.dto.category.CategoryResponse;
import com.dungnb.be.dto.category.CreateCategoryRequest;
import com.dungnb.be.dto.category.UpdateCategoryRequest;
import com.dungnb.be.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)  // ← Changed: ignore slug
    @Mapping(target = "parent", ignore = true)  // ← Set in service
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Category toCategory(CreateCategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)  // ← Changed: ignore slug
    @Mapping(target = "parent", ignore = true)  // ← Set in service
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategory(@MappingTarget Category category, UpdateCategoryRequest request);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    @Mapping(target = "children", ignore = true)
    CategoryResponse toCategoryResponse(Category category);

    // Trường hợp có children
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    @Mapping(target = "children", source = "children")
    CategoryResponse toCategoryResponseWithChildren(Category category);

    default List<CategoryResponse> mapChildren(List<Category> children) {
        if (children == null || children.isEmpty()) {
            return null;
        }
        return children.stream()
                .map(this::toCategoryResponse) // Dùng method không có children
                .collect(java.util.stream.Collectors.toList());
    }
}
