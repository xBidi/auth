package com.spring.server.repository;

import com.spring.server.model.entity.SessionToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Token repository
 *
 * @author diegotobalina
 */
@Repository
public interface SessionTokenRepository extends MongoRepository<SessionToken, String> {
  Optional<SessionToken> findByToken(String tokenString);
}
