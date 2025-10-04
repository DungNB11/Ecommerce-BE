package com.dungnb.be.controller;

import com.dungnb.be.dto.ApiResponse;
import com.dungnb.be.dto.category.CategoryResponse;
import com.dungnb.be.dto.category.CreateCategoryRequest;
import com.dungnb.be.dto.category.UpdateCategoryRequest;
import com.dungnb.be.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class AdminCategoryController extends BaseController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) throws BadRequestException {
        CategoryResponse category = categoryService.createCategory(request);
        return createdResponse(category, "Category created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdateCategoryRequest request)
    throws BadRequestException {
        CategoryResponse category = categoryService.updateCategory(id, request);
        return updatedResponse(category, "Category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) throws BadRequestException {
        categoryService.deleteCategory(id);
        return successMessage("Category deleted successfully");
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return successMessage("Category deactivated successfully");
    }
}
