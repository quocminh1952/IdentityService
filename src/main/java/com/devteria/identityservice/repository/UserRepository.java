package com.devteria.identityservice.repository;

import com.devteria.identityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // sử dụng JPA : existBy / findBy + .....


    Boolean existsByUsername(String username);

    // Optional<User> có thể là null hoặc User
    Optional<User> findByUsername(String username);
}
