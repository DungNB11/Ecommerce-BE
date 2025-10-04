package com.dungnb.be.mapper;

import com.dungnb.be.dto.brand.BrandResponse;
import com.dungnb.be.dto.brand.CreateBrandRequest;
import com.dungnb.be.dto.brand.UpdateBrandRequest;
import com.dungnb.be.entity.Brand;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)  // ← Changed: ignore slug
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Brand toBrand(CreateBrandRequest request);

    // Update - Không map slug, sẽ set trong Service nếu cần
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)  // ← Changed: ignore slug
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBrand(@MappingTarget Brand brand, UpdateBrandRequest request);

    // To Response
    @Mapping(target = "productCount", ignore = true)
    BrandResponse toBrandResponse(Brand brand);
}
