package com.music.lover.hometask.repository;

import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsByAmgArtistIdAndArtistUsersContaining(Long amgArtistId, User user);

    Optional<Artist> findByAmgArtistId(Long amgArtistId);

}
