package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Scope entity
 *
 * @author diegotobalina
 */
@Entity @Getter @NoArgsConstructor @ToString @Table(name = "spring_scope") public class Scope {
    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String description;
    private String value;

    public Scope(String name, String description, String value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
}
