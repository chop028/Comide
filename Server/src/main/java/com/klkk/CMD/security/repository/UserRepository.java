package com.klkk.CMD.security.repository;

import com.klkk.CMD.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleId(String googleId);

    List<User> findByNameContaining(String name);
}