package com.music.lover.hometask.configuration;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.time.albums}")
    private long cacheTimeAlbums;

    @Value("${cache.size.albums}")
    private long cacheSizeAlbums;

    @Value("${cache.time.artists}")
    private long cacheTimeArtists;

    @Value("${cache.size.artists}")
    private long cacheSizeArtists;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(final String name) {
                switch (name) {
                    case "artistAlbumsCache":
                        return new ConcurrentMapCache(name,
                                CacheBuilder.newBuilder()
                                        .expireAfterWrite(cacheTimeAlbums, TimeUnit.HOURS)
                                        .maximumSize(cacheSizeAlbums)
                                        .build()
                                        .asMap(), false
                        );
                    case "artistsCache":
                        return new ConcurrentMapCache(name,
                                CacheBuilder.newBuilder()
                                        .expireAfterWrite(cacheTimeArtists, TimeUnit.HOURS)
                                        .maximumSize(cacheSizeArtists)
                                        .build()
                                        .asMap(), false
                        );
                    default:
                        return new ConcurrentMapCache(name);
                }
            }
        };
    }
}
