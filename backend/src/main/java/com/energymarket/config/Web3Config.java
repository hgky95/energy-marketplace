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

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class Web3Config {
    @Value("${blockchain.node.url}")
    private String blockchainNodeUrl;
    
    // @Value("${blockchain.wallet.private-key}")
    // private String privateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(blockchainNodeUrl));
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
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    }
    
} 