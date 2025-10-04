package com.dungnb.be.repository;

import com.dungnb.be.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
    List<Category> findByParentIsNullOrderByDisplayOrderAsc();
    List<Category> findByParentIdOrderByDisplayOrderAsc(Long parentId);
    List<Category> findByIsActiveOrderByDisplayOrderAsc(Boolean isActive);
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL AND c.isActive IS TRUE ORDER BY c.displayOrder")
    List<Category> findAllParentCategoriesWithChildren();
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId ORDER BY c.displayOrder")
    List<Category> findByParentIdWithProducts(Long parentId);
}
