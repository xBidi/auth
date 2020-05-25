package com.example.demo.repository;

import com.example.demo.model.entity.ResetPasswordToken;
import com.example.demo.model.entity.SessionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Token repository
 *
 * @author diegotobalina
 */
@Repository public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
    Optional<ResetPasswordToken> findByToken(String tokenString);
}
