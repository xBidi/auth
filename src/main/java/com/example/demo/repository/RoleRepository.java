package com.example.demo.repository;

import com.example.demo.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Role repository
 *
 * @author diegotobalina
 */
@Repository public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByValue(String value);
}
