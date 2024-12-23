package com.energymarket.dto;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class NFTMetadataDto {
    private String description;
    private String image;
    private List<NFTAttributeDto> attributes;
}