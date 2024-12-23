package com.energymarket.dto;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class NFTAttributeDto {
    private String trait_type;
    private String value;
    private String unit;
    private String fullValue;
}