package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity @NoArgsConstructor @Getter @Setter public class Token {

    @Id private String id;
    private Timestamp expeditionDate;
    private Timestamp expirationDate;

    public Token(String id, Timestamp expeditionDate, Timestamp expirationDate) {
        this.id = id;
        this.expeditionDate = expeditionDate;
        this.expirationDate = expirationDate;
    }
}
