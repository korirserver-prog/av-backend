package com.game.aviator.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByName(String name);

    boolean existsByUsername(String username);

//    boolean existsByEmail(String email);
}