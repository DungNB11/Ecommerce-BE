package com.dungnb.be.dto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;
    private String description;
    private Long parentId;
    private String imageUrl;
    @Min(value = 0, message = "Display order must be positive")
    private Integer displayOrder = 0;
}
