package com.energymarket.service;

import com.energymarket.contracts.EnergyMarketplace;
import com.energymarket.contracts.EnergyMarketplace.NFTSoldEventResponse;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.abi.EventEncoder;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BlockchainEventListenerService {
    private final Web3j web3j;
    private final EnergyMarketplace marketplace;
    private final MarketplaceService marketplaceService;
    private final List<Disposable> disposables = new ArrayList<>();

    public BlockchainEventListenerService(
        Web3j web3j,
        EnergyMarketplace marketplace,
        MarketplaceService marketplaceService
    ) {
        this.web3j = web3j;
        this.marketplace = marketplace;
        this.marketplaceService = marketplaceService;
    }

    @PostConstruct
    public void init() {
        subscribeToNFTSoldEvents();
    }

    private void subscribeToNFTSoldEvents() {
        log.info("Subscribing to NFTSold events for contract {}", marketplace.getContractAddress());
        try {
            EthFilter filter = new EthFilter(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST,
                marketplace.getContractAddress()
            );
            
            filter.addSingleTopic(EventEncoder.encode(EnergyMarketplace.NFTSOLD_EVENT));

            Disposable disposable = marketplace.nFTSoldEventFlowable(filter)
                .doOnSubscribe(d -> log.info("Subscribed to NFTSold events"))
                .doOnNext(event -> log.info("Received NFTSold event"))
                .doOnError(error -> log.error("Error in event subscription", error))
                .subscribe(
                    this::handleNFTSoldEvent,
                    error -> {
                        log.error("Error in NFTSold event subscription", error);
                        resubscribe();
                    }
                );

            disposables.add(disposable);
            log.info("Successfully subscribed to NFTSold events");
        } catch (Exception e) {
            log.error("Failed to subscribe to NFTSold events", e);
            resubscribe();
        }
    }

    private void resubscribe() {
        try {
            Thread.sleep(5000);
            log.info("Attempting to resubscribe to events...");
            cleanup();
            subscribeToNFTSoldEvents();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Resubscription interrupted", e);
        }
    }

    private void handleNFTSoldEvent(NFTSoldEventResponse event) {
        try {
            BigInteger tokenId = event.tokenId.getValue();
            log.info("NFT Sold Event - TokenId: {}, Seller: {}, Buyer: {}, Price: {}", 
                tokenId, 
                event.seller.getValue(),
                event.buyer.getValue(),
                event.price.getValue()
            );

            marketplaceService.evictItem(tokenId);
        } catch (Exception e) {
            log.error("Error handling NFTSold event", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        disposables.forEach(disposable -> {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        });
        disposables.clear();
        log.info("Cleaned up blockchain event subscriptions");
    }
} 