package com.ynov.adventures.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationDTO {

    private Integer page;

    private Integer limit;

    private Integer total;

    private Integer totalPages;
}
