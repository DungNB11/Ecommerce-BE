package com.dungnb.be.service;

import com.dungnb.be.dto.brand.BrandListResponse;
import com.dungnb.be.dto.brand.BrandResponse;
import com.dungnb.be.dto.brand.CreateBrandRequest;
import com.dungnb.be.dto.brand.UpdateBrandRequest;
import com.dungnb.be.entity.Brand;
import com.dungnb.be.exception.ResourceNotFoundException;
import com.dungnb.be.mapper.BrandMapper;
import com.dungnb.be.repository.BrandRepository;
import com.dungnb.be.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final SlugUtils slugUtils;

    // Get all brands (simple list)
    public List<BrandResponse> getAllBrands() {
        log.info("Fetching all brands");
        List<Brand> brands = brandRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return brands.stream()
                .map(this::mapToBrandResponse)
                .collect(Collectors.toList());
    }

    // Get all active brands
    public List<BrandResponse> getActiveBrands() {
        log.info("Fetching active brands");
        List<Brand> brands = brandRepository.findByIsActiveOrderByNameAsc(true);
        return brands.stream()
                .map(this::mapToBrandResponse)
                .collect(Collectors.toList());
    }

    // Get brands with pagination
    public BrandListResponse getBrandsWithPagination(
            Boolean activeOnly,
            String keyword,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        log.info("Fetching brands - page: {}, size: {}, activeOnly: {}, keyword: {}",
                page, size, activeOnly, keyword);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Brand> brandPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (activeOnly != null && activeOnly) {
                brandPage = brandRepository.searchActiveBrands(keyword, true, pageable);
            } else {
                brandPage = brandRepository.searchBrands(keyword, pageable);
            }
        } else {
            if (activeOnly != null && activeOnly) {
                brandPage = brandRepository.findByIsActive(true, pageable);
            } else {
                brandPage = brandRepository.findAll(pageable);
            }
        }

        List<BrandResponse> brandResponses = brandPage.getContent().stream()
                .map(this::mapToBrandResponse)
                .collect(Collectors.toList());

        return BrandListResponse.builder()
                .brands(brandResponses)
                .currentPage(brandPage.getNumber())
                .totalPages(brandPage.getTotalPages())
                .totalElements(brandPage.getTotalElements())
                .pageSize(brandPage.getSize())
                .build();
    }

    // Get brand by ID
    public BrandResponse getBrandById(Long id) {
        log.info("Fetching brand with id: {}", id);
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        return mapToBrandResponse(brand);
    }

    // Get brand by slug
    public BrandResponse getBrandBySlug(String slug) {
        log.info("Fetching brand with slug: {}", slug);
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with slug: " + slug));
        return mapToBrandResponse(brand);
    }

    // Create brand
    public BrandResponse createBrand(CreateBrandRequest request) throws BadRequestException {
        log.info("Creating new brand: {}", request.getName());

        // Check if name already exists
        if (brandRepository.existsByName(request.getName())) {
            throw new BadRequestException("Brand with name '" + request.getName() + "' already exists");
        }

        String slug = slugUtils.generateUniqueSlug(
                request.getName(),
                brandRepository::existsBySlug
        );

        Brand brand = brandMapper.toBrand(request);
        brand.setSlug(slug);

        Brand savedBrand = brandRepository.save(brand);
        log.info("Brand created successfully with id: {}", savedBrand.getId());

        return mapToBrandResponse(savedBrand);
    }

    // Update brand
    public BrandResponse updateBrand(Long id, UpdateBrandRequest request) throws BadRequestException {
        log.info("Updating brand with id: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        // Check if new name conflicts with existing brand
        if (!brand.getName().equals(request.getName())
                && brandRepository.existsByName(request.getName())) {
            throw new BadRequestException("Brand with name '" + request.getName() + "' already exists");
        }

        // Update slug if name changed
        if (!brand.getName().equals(request.getName())) {
            String newSlug = slugUtils.generateUniqueSlug(
                    request.getName(),
                    brand.getSlug(), // Current slug
                    brandRepository::existsBySlug
            );
            brand.setSlug(newSlug);
        }

        brandMapper.updateBrand(brand, request);

        Brand updatedBrand = brandRepository.save(brand);
        log.info("Brand updated successfully with id: {}", id);

        return brandMapper.toBrandResponse(updatedBrand);
    }

    // Delete brand (hard delete)
    public void deleteBrand(Long id) throws BadRequestException {
        log.info("Deleting brand with id: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        // Check if brand has products
        if (!brand.getProducts().isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete brand with products. " +
                            "Remove or reassign products first. " +
                            "Current product count: " + brand.getProducts().size()
            );
        }

        brandRepository.delete(brand);
        log.info("Brand deleted successfully with id: {}", id);
    }

    // Deactivate brand (soft delete)
    public void deactivateBrand(Long id) {
        log.info("Deactivating brand with id: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        brand.setIsActive(false);
        brandRepository.save(brand);

        log.info("Brand deactivated successfully with id: {}", id);
    }

    // Activate brand
    public void activateBrand(Long id) {
        log.info("Activating brand with id: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        brand.setIsActive(true);
        brandRepository.save(brand);

        log.info("Brand activated successfully with id: {}", id);
    }

    // Helper: Map to response with product count
    private BrandResponse mapToBrandResponse(Brand brand) {
        BrandResponse response = brandMapper.toBrandResponse(brand);
        Long productCount = brandRepository.countProductsByBrandId(brand.getId());
        response.setProductCount(productCount.intValue());
        return response;
    }
}
