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
 * Role entity
 *
 * @author diegotobalina
 */
@NoArgsConstructor @Getter @ToString @Document(collection = "spring_role") public class Role extends Auditable{
    @Id private String id;
    private String name;
    private String description;
    @Indexed private String value;

    public Role(String name, String description, String value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
}
