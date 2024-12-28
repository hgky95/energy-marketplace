package com.energymarket.service;

import com.energymarket.dto.NFTMetadataDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Service
public class NFTMetadataService {
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public NFTMetadataService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "nftMetadataCache", key = "#p0")
    public NFTMetadataDto fetchMetadata(String uri) {
        try {
            Request request = new Request.Builder()
                .url(uri)
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

    @CacheEvict(value = "nftMetadataCache", allEntries = true)
    @Scheduled(fixedRateString = "${cache.evict.rate:3600000}")
    public void evictMetadataCache() {
        log.info("Evicting NFT metadata cache");
    }
} 