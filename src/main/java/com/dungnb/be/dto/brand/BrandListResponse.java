package com.dungnb.be.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandListResponse {
    private List<BrandResponse> brands;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
}
