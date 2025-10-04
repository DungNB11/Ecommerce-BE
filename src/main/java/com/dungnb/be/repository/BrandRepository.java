package com.dungnb.be.repository;

import com.dungnb.be.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    List<Brand> findByIsActiveOrderByNameAsc(Boolean isActive);

    Page<Brand> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT b FROM Brand b WHERE " + "LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Brand> searchBrands(String keyword, Pageable pageable);

    @Query("SELECT b FROM Brand b WHERE b.isActive = :isActive AND " + "LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Brand> searchActiveBrands(String keyword, Boolean isActive, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.brand.id = :brandId")
    Long countProductsByBrandId(Long brandId);
}
