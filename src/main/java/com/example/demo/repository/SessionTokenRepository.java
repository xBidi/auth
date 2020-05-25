package com.example.demo.repository;

import com.example.demo.model.entity.SessionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Token repository
 *
 * @author diegotobalina
 */
@Repository public interface SessionTokenRepository extends JpaRepository<SessionToken, String> {
    Optional<SessionToken> findByToken(String tokenString);
}
