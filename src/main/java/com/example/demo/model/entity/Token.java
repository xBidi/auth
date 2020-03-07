package com.example.demo.model.entity;

import com.example.demo.security.AttributeEncryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;


/**
 * Token entity
 *
 * @author diegotobalina
 */
@Entity @NoArgsConstructor @Getter @Setter @ToString public class Token {

    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Convert(converter = AttributeEncryptor.class) private String token;
    private Timestamp expeditionDate;
    private Timestamp expirationDate;

    public Token(String token, Timestamp expeditionDate, Timestamp expirationDate) {
        this.token = token;
        this.expeditionDate = expeditionDate;
        this.expirationDate = expirationDate;
    }
}
