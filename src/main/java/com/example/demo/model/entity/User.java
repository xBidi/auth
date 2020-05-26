package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User entity
 *
 * @author diegotobalina
 */
@Getter @Setter @NoArgsConstructor @Document(collection = "spring_user") @ToString
public class User extends Auditable{

    @Id private String id;
    @Indexed(unique = true) private String username;
    @Indexed(unique = true) private String email;

    private String password;
    private Boolean emailVerified = false;
    private List<SessionToken> sessionTokens = new ArrayList<>();
    private List<ResetPasswordToken> resetPasswordTokens = new ArrayList<>();
    private List<VerifyEmailToken> verifyEmailTokens = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private List<Scope> scopes = new ArrayList<>();

    public User(String username, String email, String password, Boolean emailVerified,
        List<Role> roles, List<Scope> scopes) {
        this.username = username;
        this.email = email;
        this.setPassword(password);
        this.roles = roles;
        this.scopes = scopes;
        this.emailVerified = emailVerified;
    }

    public User(String id, String username, String email, String password, Boolean emailVerified,
        List<Role> roles, List<Scope> scopes) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.setPassword(password);
        this.roles = roles;
        this.scopes = scopes;
        this.emailVerified = emailVerified;
    }


    public void setPassword(String password) {
        this.password = password;
        this.hash();
    }

    public void hash() {
        String regex = "^\\$2[ayb]\\$.{56}$"; // regex bcrypt
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(this.getPassword());
        if (!m.matches()) {
            this.setPassword(hash(this.password));
        }
    }

    public String hash(String p) {
        BCryptPasswordEncoder bcoder = new BCryptPasswordEncoder(6);
        return bcoder.encode(p);
    }


}
