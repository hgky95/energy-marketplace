package com.energymarket.controller;

import com.energymarket.dto.NFTDto;
import com.energymarket.service.NFTService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/nfts")
@RequiredArgsConstructor
public class NFTController {
    private final NFTService nftService;
    
    @GetMapping
    public Page<NFTDto> getListedNFTs(
        @RequestParam(defaultValue = "home") String section,
        @RequestParam(required = false) String account,
        Pageable pageable
    ) {
        return nftService.getNFTs(section, account, pageable);
    }
} 