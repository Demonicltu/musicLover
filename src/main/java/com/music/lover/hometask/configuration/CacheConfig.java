package com.music.lover.hometask.configuration;

import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;
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

    private final long cacheTimeAlbums;

    private final long cacheSizeAlbums;

    private final long cacheTimeArtists;

    private final long cacheSizeArtists;

    public CacheConfig(
            @Value("${cache.time.albums}") long cacheTimeAlbums,
            @Value("${cache.size.albums}") long cacheSizeAlbums,
            @Value("${cache.time.artists}") long cacheTimeArtists,
            @Value("${cache.size.artists}") long cacheSizeArtists) {
        this.cacheTimeAlbums = cacheTimeAlbums;
        this.cacheSizeAlbums = cacheSizeAlbums;
        this.cacheTimeArtists = cacheTimeArtists;
        this.cacheSizeArtists = cacheSizeArtists;
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @NotNull
            @Override
            protected Cache createConcurrentMapCache(@NotNull final String name) {
                switch (name) {
                    case "albumsCache":
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
