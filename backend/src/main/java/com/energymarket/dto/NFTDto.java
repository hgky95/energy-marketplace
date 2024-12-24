package com.energymarket.dto;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class NFTDto {
    private Long id;
    private String title;
    private String price;
    private Integer energyAmount;
    private String seller;
    private String image;
    private String description;
    private boolean isActive;
    private List<NFTAttributeDto> attributes;
}