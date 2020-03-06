package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Scope entity
 *
 * @author diegotobalina
 */
@Entity @Getter @NoArgsConstructor public class Scope {
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
