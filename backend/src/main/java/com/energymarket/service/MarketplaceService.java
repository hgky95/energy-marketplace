package com.energymarket.service;

import com.energymarket.contracts.EnergyMarketplace;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;
import lombok.extern.slf4j.Slf4j;
import java.math.BigInteger;

@Slf4j
@Service
public class MarketplaceService {
    private final EnergyMarketplace marketplace;

    public MarketplaceService(EnergyMarketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Cacheable(value = "marketplaceItems", key = "#p0")
    public MarketplaceItem fetchMarketplaceItem(BigInteger tokenId) throws Exception {
        var item = marketplace.items(new Uint256(tokenId)).send();
        
        return new MarketplaceItem(
            item.component4().toString(), // seller
            item.component5().getValue(), // isActive
            item.component2().getValue(), // price
            item.component3().getValue()  // energyAmount
        );
    }

    @CacheEvict(value = "marketplaceItems", key = "#p0")
    public void evictItem(BigInteger tokenId) {
        log.info("Evicting cache for marketplace item #{}", tokenId);
    }

    public record MarketplaceItem(
        String seller, 
        boolean isActive, 
        BigInteger price, 
        BigInteger energyAmount
    ) {}
} 