package com.energymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import com.energymarket.dto.NFTDto;
import com.energymarket.dto.NFTMetadataDto;
import com.energymarket.service.MarketplaceService.MarketplaceItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.PostConstruct;

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
    private final ExecutorService executorService;
    private final EnergyMarketplace marketplace;
    private EnergyNFT nft;
    private final ConcurrentHashMap<String, BigInteger> itemCountCache = new ConcurrentHashMap<>();
    private static final List<NFTDto> DEFAULT_NFT_SUBLIST = new ArrayList<>();
    private final NFTMetadataService nftMetadataService;
    private final MarketplaceService marketplaceService;
    
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
        ObjectMapper objectMapper,
        NFTMetadataService nftMetadataService,
        MarketplaceService marketplaceService,
        EnergyMarketplace marketplace
    ) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.nftMetadataService = nftMetadataService;
        this.executorService = Executors.newFixedThreadPool(3); //Can be adjusted but considering on system resources and node provider's throughput (429 issue)
        this.marketplaceService = marketplaceService;
        this.marketplace = marketplace;
    }
    
    @PostConstruct
    private void init() {
        this.nft = EnergyNFT.load(
            nftAddress, web3j, credentials, new DefaultGasProvider()
        );
    }
    
    public Page<NFTDto> getNFTs(String section, String account, Pageable pageable) {
        log.info("Fetching NFTs for section: {}, account: {}", section, account);
        try {
            Uint256 currentItemCount = marketplace.itemCount().send();
            itemCountCache.put("itemCount", currentItemCount.getValue());
            
            Page<NFTDto> nfts = fetchAndProcessNFTs(section, account, pageable, currentItemCount.getValue());
            return nfts;
        } catch (Exception e) {
            log.error("Error fetching NFTs", e);
            throw new RuntimeException("Failed to fetch NFTs", e);
        }
    }
    
    private Page<NFTDto> fetchAndProcessNFTs(
        String section,
        String account,
        Pageable pageable,
        BigInteger totalItems
    ) throws Exception {
        if (totalItems.equals(BigInteger.ZERO)) {
            return new PageImpl<>(DEFAULT_NFT_SUBLIST, pageable, 0);
        }

        AtomicInteger totalValidNfts = new AtomicInteger(0);
        List<CompletableFuture<NFTDto>> futures = new ArrayList<>();
        List<NFTDto> pageItems = new ArrayList<>();

        // Count total valid NFTs using parallel processing
        int batchSize = 20;
        List<CompletableFuture<Integer>> countFutures = new ArrayList<>();
        
        for (int i = 1; i <= totalItems.intValue(); i += batchSize) {
            final int start = i;
            final int end = Math.min(i + batchSize - 1, totalItems.intValue());
            
            CompletableFuture<Integer> batchCount = CompletableFuture.supplyAsync(() -> {
                int count = 0;
                for (int j = start; j <= end; j++) {
                    try {
                        BigInteger tokenId = BigInteger.valueOf(j);
                        MarketplaceItem item = marketplaceService.fetchMarketplaceItem(tokenId);
                        
                        if (shouldIncludeNFT(section, account, item.seller(), item.isActive(), tokenId)) {
                            count++;
                        }
                    } catch (Exception e) {
                        log.error("Error processing token #{}", j, e);
                    }
                }
                return count;
            }, executorService);
            
            countFutures.add(batchCount);
        }
        
        BigInteger currentId = BigInteger.ONE;
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int currentCount = 0;
        int collectedItems = 0;

        // Collect only the items for the requested page
        log.info("Start second pass");
        while (currentId.compareTo(totalItems) <= 0 && collectedItems < pageable.getPageSize()) {
            BigInteger tokenId = currentId;

            MarketplaceItem item = marketplaceService.fetchMarketplaceItem(tokenId);
            
            if (shouldIncludeNFT(section, account, item.seller(), item.isActive(), tokenId)) {
                if (currentCount >= start) {
                    CompletableFuture<NFTDto> future = CompletableFuture.supplyAsync(() -> {
                        try {
                            String uri = nft.tokenURI(new Uint256(tokenId)).send().getValue();
                            NFTMetadataDto metadata = nftMetadataService.fetchMetadata(uri);
                            
                            return NFTDto.builder()
                                .id(tokenId.longValue())
                                .title("Energy NFT #" + tokenId)
                                .price(formatEther(item.price()) + " ETH")
                                .energyAmount(item.energyAmount().intValue())
                                .seller(item.seller())
                                .image(metadata.getImage())
                                .description(metadata.getDescription())
                                .attributes(metadata.getAttributes())
                                .isActive(item.isActive())
                                .build();
                        } catch (Exception e) {
                            log.error("Error processing NFT #{}", tokenId, e);
                            return null;
                        }
                    }, executorService);
                    
                    futures.add(future);
                    collectedItems++;
                }
                currentCount++;
            }
            currentId = currentId.add(BigInteger.ONE);
        }

        // Wait for all counting tasks to complete and sum the results
        int totalValidNftsCount = countFutures.stream()
            .map(CompletableFuture::join)
            .mapToInt(Integer::intValue)
            .sum();
        totalValidNfts.set(totalValidNftsCount);

        pageItems = futures.stream()
            .map(CompletableFuture::join)
            .filter(dto -> dto != null)
            .collect(Collectors.toList());

        return new PageImpl<>(pageItems, pageable, totalValidNfts.get());
    }
    
    private boolean shouldIncludeNFT(
        String section, 
        String account, 
        String seller, 
        boolean isActive,
        BigInteger tokenId
    ) {
        try {
            if ("home".equals(section)) {
                return isActive && (account == null || account.isEmpty() || !seller.equalsIgnoreCase(account));
            } else if ("listing".equals(section)) {
                return isActive && seller.equalsIgnoreCase(account);
            } else if ("purchased".equals(section)) {
                if (!isActive && !seller.equalsIgnoreCase(account)) {
                    String currentOwner = nft.ownerOf(new Uint256(tokenId)).send().toString();
                    return currentOwner.equalsIgnoreCase(account);
                }
            }
        } catch (Exception e) {
            log.error("Error checking NFT inclusion for #{}", tokenId, e);
        }
        return false;
    }
    
    private String formatEther(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER).toString();
    }

    @PreDestroy
    public void cleanup() {
        executorService.shutdown();
    }
}