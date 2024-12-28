package com.energymarket.dto;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTAttributeDto {
    private String trait_type;
    private String value;
    private String unit;
    private String fullValue;
}