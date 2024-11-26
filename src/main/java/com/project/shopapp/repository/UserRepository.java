package com.project.shopapp.repository;

import com.project.shopapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

//    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

//    Optional<User> findByEmail(String email);
}
