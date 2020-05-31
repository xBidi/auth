package com.spring.server.repository;

import com.spring.server.model.entity.ResetPasswordToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Token repository
 *
 * @author diegotobalina
 */
@Repository
public interface ResetPasswordTokenRepository extends MongoRepository<ResetPasswordToken, String> {
  Optional<ResetPasswordToken> findByToken(String tokenString);
}
