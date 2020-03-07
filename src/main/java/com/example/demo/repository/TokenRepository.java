package com.example.demo.repository;

import com.example.demo.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Token repository
 *
 * @author diegotobalina
 */
@Repository public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String tokenString);
}
