package com.energymarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import com.energymarket.contracts.EnergyMarketplace;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class Web3Config {
    @Value("${blockchain.node.url}")
    private String blockchainNodeUrl;
    
    @Value("${contract.marketplace.address}")
    private String marketplaceAddress;

    @Bean
    public Web3j web3j() {
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build();
        
        return Web3j.build(new HttpService(blockchainNodeUrl, client));
    }
    
    @Bean
    public Credentials credentials() {
        try {
            Credentials dummyCredentials = Credentials.create(Keys.createEcKeyPair());
            return dummyCredentials;
        } catch (Exception e) {
            log.error("Error creating dummy credentials", e);
        }
        return null;
    }
    
    @Bean
    public ContractGasProvider gasProvider() {
        BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L); // 20 Gwei
        BigInteger gasLimit = BigInteger.valueOf(4_300_000L);
        return new StaticGasProvider(gasPrice, gasLimit);
    }
    
    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build();
    }
    
    @Bean
    public EnergyMarketplace energyMarketplace(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return EnergyMarketplace.load(
            marketplaceAddress,
            web3j,
            credentials,
            gasProvider
        );
    }
} 