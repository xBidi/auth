package com.example.demo.repository;

import com.example.demo.model.entity.ResetPasswordToken;
import com.example.demo.model.entity.VerifyEmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Token repository
 *
 * @author diegotobalina
 */
@Repository public interface VerifyEmailTokenRepository extends JpaRepository<VerifyEmailToken, String> {
    Optional<VerifyEmailToken> findByToken(String tokenString);
}
