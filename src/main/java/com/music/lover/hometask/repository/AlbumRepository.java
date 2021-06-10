package com.music.lover.hometask.repository;

import com.music.lover.hometask.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByArtistAmgArtistId(Long amgArtistId);

    void deleteAllByArtistAmgArtistId(Long amgArtistId);

}
