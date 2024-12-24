package com.energymarket.config;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheMonitoringConfig {
    private final CacheManager cacheManager;

    public CacheMonitoringConfig(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void logCacheStatistics() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        cacheNames.forEach(cacheName -> {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (cache != null) {
                CacheStats stats = cache.getNativeCache().stats();
                log.info("Cache Statistics for {}: Hits={}, Misses={}, Size={}", 
                    cacheName, stats.hitCount(), stats.missCount(), cache.getNativeCache().estimatedSize()
                );
            }
        });
    }
} 