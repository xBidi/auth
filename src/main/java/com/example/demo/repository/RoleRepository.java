package com.example.demo.repository;

import com.example.demo.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByValue(String value);
}
