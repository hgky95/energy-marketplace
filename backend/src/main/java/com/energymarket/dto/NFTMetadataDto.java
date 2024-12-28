package com.energymarket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTMetadataDto {
    private String description;
    private String image;
    private List<NFTAttributeDto> attributes;
}