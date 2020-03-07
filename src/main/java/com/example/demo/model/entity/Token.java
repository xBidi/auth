package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;


/**
 * Token entity
 *
 * @author diegotobalina
 */
@Entity @NoArgsConstructor @Getter @Setter @ToString public class Token {

    @Id private String id;
    private Timestamp expeditionDate;
    private Timestamp expirationDate;

    public Token(String id, Timestamp expeditionDate, Timestamp expirationDate) {
        this.id = id;
        this.expeditionDate = expeditionDate;
        this.expirationDate = expirationDate;
    }
}
