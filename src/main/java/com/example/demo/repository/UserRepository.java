package com.example.demo.repository;

import com.example.demo.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository
 *
 * @author diegotobalina
 */
@Repository public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findBySessionTokensToken(String token);

    Optional<User> findByResetPasswordTokensToken(String token);

    Optional<User> findByVerifyEmailTokensToken(String token);

}
