package com.dungnb.be.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Integer productCount;
}
