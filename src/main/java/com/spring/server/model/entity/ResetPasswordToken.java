package com.spring.server.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 * Token entity
 *
 * @author diegotobalina
 */
@NoArgsConstructor @Getter @Setter @ToString @Document(collection = "spring_reset_password_token")
public class ResetPasswordToken extends Auditable{

    @Id private String id;
    @Indexed private String token;
    private Date expeditionDate;
    private Date expirationDate;

    public ResetPasswordToken(String token, Date expeditionDate, Date expirationDate) {
        this.token = token;
        this.expeditionDate = expeditionDate;
        this.expirationDate = expirationDate;
    }
}
