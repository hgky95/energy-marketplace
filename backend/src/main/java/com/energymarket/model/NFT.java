package com.energymarket.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "nfts")
public class NFT {
    @Id
    private BigInteger tokenId;
    
    private String tokenURI;
    private BigInteger energyAmount;
    private String owner;
    private BigInteger price;
    private boolean isListed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 