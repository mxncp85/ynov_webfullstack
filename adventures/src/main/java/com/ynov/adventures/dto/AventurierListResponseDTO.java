package com.ynov.adventures.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AventurierListResponseDTO {

    private List<AventurierDTO> data;

    private PaginationDTO pagination;
}
