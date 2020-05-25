package com.example.demo.model.entity;

import com.example.demo.security.AttributeEncryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * Token entity
 *
 * @author diegotobalina
 */
@Entity @NoArgsConstructor @Getter @Setter @ToString @Table(name = "spring_reset_password_token")
public class ResetPasswordToken {

    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Convert(converter = AttributeEncryptor.class) private String token;
    private Timestamp expeditionDate;
    private Timestamp expirationDate;

    public ResetPasswordToken(String token, Timestamp expeditionDate, Timestamp expirationDate) {
        this.token = token;
        this.expeditionDate = expeditionDate;
        this.expirationDate = expirationDate;
    }
}
