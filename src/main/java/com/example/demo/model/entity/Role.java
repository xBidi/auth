package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Role entity
 *
 * @author diegotobalina
 */
@Entity @NoArgsConstructor @Getter @ToString @Table(name = "spring_role") public class Role {
    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String description;
    private String value;

    public Role(String name, String description, String value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
}
