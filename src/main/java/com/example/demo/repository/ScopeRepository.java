package com.example.demo.repository;

import com.example.demo.model.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Scope repository
 *
 * @author diegotobalina
 */
@Repository public interface ScopeRepository extends JpaRepository<Scope, String> {
    Optional<Scope> findByValue(String value);
}
