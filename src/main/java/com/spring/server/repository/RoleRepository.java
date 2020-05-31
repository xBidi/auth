package com.spring.server.repository;

import com.spring.server.model.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Role repository
 *
 * @author diegotobalina
 */
@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByValue(String value);
}
