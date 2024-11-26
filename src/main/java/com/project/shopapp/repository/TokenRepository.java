package com.project.shopapp.repository;

import com.project.shopapp.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository  extends JpaRepository<Token, String> {
}
