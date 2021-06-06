package com.music.lover.hometask.repository;

import com.music.lover.hometask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUuid(String uuid);

}
