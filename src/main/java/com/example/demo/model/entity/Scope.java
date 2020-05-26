package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Scope entity
 *
 * @author diegotobalina
 */
@Getter @NoArgsConstructor @ToString @Document(collection = "spring_scope") public class Scope extends Auditable{
    @Id private String id;
    private String name;
    private String description;
    @Indexed private String value;

    public Scope(String name, String description, String value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
}
