package com.energymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.energymarket.dto.NFTDto;
import com.energymarket.dto.NFTMetadataDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.energymarket.contracts.EnergyMarketplace;
import com.energymarket.contracts.EnergyNFT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class NFTService {
    private final Web3j web3j;
    private final Credentials credentials;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, BigInteger> itemCountCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> nftStatusCache = new ConcurrentHashMap<>();
    private static final List<NFTDto> DEFAULT_NFT_SUBLIST = new ArrayList<>();
    
    @Value("${contract.marketplace.address}")
    private String marketplaceAddress;
    
    @Value("${contract.nft.address}")
    private String nftAddress;
    
    @Value("${ipfs.gateway.url}")
    private String ipfsGateway;
    
    public NFTService(
        Web3j web3j, 
        Credentials credentials, 
        OkHttpClient httpClient,
        ObjectMapper objectMapper
    ) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }
    
    @Cacheable(value = "nftCache", 
               key = "'nfts:' + #section + ':' + #account + ':' + #pageable.pageNumber + ':' + #pageable.pageSize", 
               condition = "#root.target.isValidCacheForSection(#section, #account)")
    public Page<NFTDto> getNFTs(String section, String account, Pageable pageable) {
        log.info("Fetching NFTs for section: {}, account: {}", section, account);
        log.info("Page size: {}, offset: {}", pageable.getPageSize(), pageable.getOffset());
        try {
            EnergyMarketplace marketplace = EnergyMarketplace.load(
                marketplaceAddress, web3j, credentials, new DefaultGasProvider()
            );
            
            Uint256 currentItemCount = marketplace.itemCount().send();
            itemCountCache.put("itemCount", currentItemCount.getValue());
            
            return fetchAndProcessNFTs(section, account, pageable, marketplace);
        } catch (Exception e) {
            log.error("Error fetching NFTs", e);
            throw new RuntimeException("Failed to fetch NFTs", e);
        }
    }
    
    public boolean isValidCacheForSection(String section, String account) {
        log.info("Checking cache validity for section: {} and account: {}", section, account);
        try {
            EnergyMarketplace marketplace = EnergyMarketplace.load(
                marketplaceAddress, web3j, credentials, new DefaultGasProvider()
            );
            
            BigInteger currentCount = marketplace.itemCount().send().getValue();
            BigInteger cachedCount = itemCountCache.getOrDefault("itemCount", BigInteger.ZERO);
            
            if (!currentCount.equals(cachedCount)) {
                return false;
            }
            
            // For sections that depend on NFT ownership/status, check the latest state
            String cacheKey = getCacheKey(section, account);
            String cachedStatus = nftStatusCache.getOrDefault(cacheKey, "");
            String currentStatus = calculateStatusHash(section, account, marketplace);
            if (cachedStatus.isEmpty()) {
                nftStatusCache.put(cacheKey, currentStatus);
            }
            
            return cachedStatus.equals(currentStatus);
        } catch (Exception e) {
            log.error("Error validating cache for section: {} and account: {}", section, account, e);
            return false;
        }
    }
    
    private String getCacheKey(String section, String account) {
        return String.format("%s:%s", section, account);
    }
    
    private String calculateStatusHash(String section, String account, EnergyMarketplace marketplace) throws Exception {
        StringBuilder status = new StringBuilder();
        Uint256 itemCount = marketplace.itemCount().send();
        
        // Only check relevant NFTs based on section
        for (BigInteger i = BigInteger.ONE; i.compareTo(itemCount.getValue()) <= 0; i = i.add(BigInteger.ONE)) {
            var item = marketplace.items(new Uint256(i)).send();
            boolean isActive = item.component5().getValue();
            String seller = item.component4().toString();
            
            if ("home".equals(section) && !seller.equalsIgnoreCase(account)) {
                status.append(i).append(":").append(isActive).append(";");
            } else if ("listing".equals(section) && seller.equalsIgnoreCase(account)) {
                status.append(i).append(":").append(isActive).append(";");
            } else if ("purchased".equals(section)) {
                EnergyNFT nft = EnergyNFT.load(nftAddress, web3j, credentials, new DefaultGasProvider());
                String owner = nft.ownerOf(new Uint256(i)).send().toString();
                status.append(i).append(":").append(owner).append(";");
            }
        }
        
        return status.toString();
    }

    @Scheduled(fixedRate = 3600000)
    @CacheEvict(value = "nftCache", allEntries = true)
    public void clearCacheIfNeeded() {
        log.debug("Clearing NFT cache...");
        itemCountCache.clear();
        nftStatusCache.clear();
    }
    
    private Page<NFTDto> fetchAndProcessNFTs(
        String section,
        String account,
        Pageable pageable,
        EnergyMarketplace marketplace
    ) throws Exception {
        EnergyNFT nft = EnergyNFT.load(
            nftAddress, web3j, credentials, new DefaultGasProvider()
        );

        List<NFTDto> allNfts = new ArrayList<>();
        Uint256 itemCount = marketplace.itemCount().send();

        for (BigInteger i = BigInteger.ONE; 
             i.compareTo(itemCount.getValue()) <= 0; 
             i = i.add(BigInteger.ONE)) {
            
            var item = marketplace.items(new Uint256(i)).send();
            
            BigInteger tokenId = item.component1().getValue();
            BigInteger price = item.component2().getValue();
            BigInteger energyAmount = item.component3().getValue();
            String seller = item.component4().toString();
            boolean isActive = item.component5().getValue();

            if (shouldIncludeNFT(section, account, seller, isActive, nft, tokenId)) {
                String uri = nft.tokenURI(new Uint256(tokenId)).send().getValue();
                NFTMetadataDto metadata = fetchMetadata(uri);
                
                NFTDto nftDto = NFTDto.builder()
                    .id(tokenId.longValue())
                    .title("Energy NFT #" + tokenId)
                    .price(formatEther(price) + " ETH")
                    .energyAmount(energyAmount.intValue())
                    .seller(seller)
                    .image(metadata.getImage())
                    .description(metadata.getDescription())
                    .attributes(metadata.getAttributes())
                    .build();
                    
                allNfts.add(nftDto);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allNfts.size());

        if (start >= end) {
            return new PageImpl<>(DEFAULT_NFT_SUBLIST, pageable, 0);
        }
        
        return new PageImpl<>(
            allNfts.subList(start, end),
            pageable,
            allNfts.size()
        );
    }
    
    private boolean shouldIncludeNFT(
        String section, 
        String account, 
        String seller, 
        boolean isActive,
        EnergyNFT nft,
        BigInteger tokenId
    ) throws Exception {
        if ("home".equals(section)) {
            return isActive && (account == null || account.isEmpty() || !seller.equalsIgnoreCase(account));
        } else if ("listing".equals(section)) {
            return isActive && seller.equalsIgnoreCase(account);
        } else if ("purchased".equals(section)) {
            if (!isActive) {
                String owner = nft.ownerOf(new Uint256(tokenId)).send().toString();
                return owner.equalsIgnoreCase(account);
            }
        }
        return false;
    }
    
    private NFTMetadataDto fetchMetadata(String uri) {
        try {
            String ipfsUri = uri.replace("ipfs://", ipfsGateway);
            
            Request request = new Request.Builder()
                .url(ipfsUri)
                .get()
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }

                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, NFTMetadataDto.class);
            }
        } catch (Exception e) {
            log.error("Error fetching metadata from {}", uri, e);
            return NFTMetadataDto.builder()
                .description("")
                .image("")
                .attributes(new ArrayList<>())
                .build();
        }
    }
    
    private String formatEther(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER).toString();
    }
}