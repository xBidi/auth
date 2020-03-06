package com.example.demo.repository;

import com.example.demo.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository public interface TokenRepository extends JpaRepository<Token, String> {
}
