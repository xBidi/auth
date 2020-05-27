package com.spring.server.repository;

import com.spring.server.model.entity.VerifyEmailToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Token repository
 *
 * @author diegotobalina
 */
@Repository public interface VerifyEmailTokenRepository extends
    MongoRepository<VerifyEmailToken, String> {
    Optional<VerifyEmailToken> findByToken(String tokenString);
}
