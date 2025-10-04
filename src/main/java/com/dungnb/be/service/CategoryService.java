package com.dungnb.be.service;

import com.dungnb.be.dto.category.CategoryResponse;
import com.dungnb.be.dto.category.CreateCategoryRequest;
import com.dungnb.be.dto.category.UpdateCategoryRequest;
import com.dungnb.be.entity.Category;
import com.dungnb.be.exception.ResourceNotFoundException;
import com.dungnb.be.mapper.CategoryMapper;
import com.dungnb.be.repository.CategoryRepository;
import com.dungnb.be.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SlugUtils slugUtils;

    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> parentCategories = categoryRepository.findByParentIsNullOrderByDisplayOrderAsc();
        return parentCategories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getActiveCategories() {
        log.info("Fetching active categories");
        List<Category> categories = categoryRepository.findAllParentCategoriesWithChildren();
        return categories.stream()
                .map(categoryMapper::toCategoryResponseWithChildren)
                .collect(Collectors.toList());
    }

    // Get category by ID
    public CategoryResponse getCategoryById(Long id) {
        log.info("Fetching category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toCategoryResponseWithChildren(category);
    }

    // Get category by slug
    public CategoryResponse getCategoryBySlug(String slug) {
        log.info("Fetching category with slug: {}", slug);
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return categoryMapper.toCategoryResponseWithChildren(category);
    }

    // Get subcategories by parent ID
    public List<CategoryResponse> getSubcategories(Long parentId) {
        log.info("Fetching subcategories for parent id: {}", parentId);

        // Verify parent exists
        categoryRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + parentId));

        List<Category> subcategories = categoryRepository.findByParentIdOrderByDisplayOrderAsc(parentId);
        return subcategories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    // Create new category
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating new category: {}", request.getName());

        // Generate unique slug using SlugUtils
        String slug = slugUtils.generateUniqueSlug(
                request.getName(),
                categoryRepository::existsBySlug
        );

        Category category = categoryMapper.toCategory(request);
        category.setSlug(slug);

        // Set parent if provided
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", savedCategory.getId());

        return categoryMapper.toCategoryResponse(savedCategory);
    }

    // Update category
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) throws BadRequestException {
        log.info("Updating category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Update slug if name changed
        if (!category.getName().equals(request.getName())) {
            String newSlug = slugUtils.generateUniqueSlug(
                    request.getName(),
                    category.getSlug(), // Current slug
                    categoryRepository::existsBySlug
            );
            category.setSlug(newSlug);
        }

        categoryMapper.updateCategory(category, request);

        // Update parent if changed
        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new BadRequestException("Category cannot be its own parent");
            }

            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with id: {}", id);

        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    // Delete category
    public void deleteCategory(Long id) throws BadRequestException {
        log.info("Deleting category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Check if category has children
        if (!category.getChildren()
                .isEmpty()) {
            throw new BadRequestException("Cannot delete category with subcategories. Delete subcategories first.");
        }

        // Check if category has products
        if (!category.getProducts()
                .isEmpty()) {
            throw new BadRequestException("Cannot delete category with products. Remove or reassign products first.");
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully with id: {}", id);
    }

    // Soft delete (deactivate)
    public void deactivateCategory(Long id) {
        log.info("Deactivating category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setIsActive(false);
        categoryRepository.save(category);

        log.info("Category deactivated successfully with id: {}", id);
    }

    // Helper: Generate slug from name
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "")
                .replaceAll("-+", "-");
    }

    // Helper: Check if targetId is a descendant of categoryId
    private boolean isDescendant(Long categoryId, Long targetId) {
        Category target = categoryRepository.findById(targetId)
                .orElse(null);
        if (target == null) return false;

        Category current = target.getParent();
        while (current != null) {
            if (current.getId()
                    .equals(categoryId)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
