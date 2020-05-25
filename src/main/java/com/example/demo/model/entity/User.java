package com.example.demo.model.entity;

import com.example.demo.model.auditor.Auditable;
import com.example.demo.security.AttributeEncryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User entity
 *
 * @author diegotobalina
 */
@Entity @Getter @Setter @NoArgsConstructor @EntityListeners(UserListener.class)
@Table(name = "spring_user") @ToString public class User extends Auditable<String> {

    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(unique = true) private String username;
    @Column(unique = true) private String email;
    @Convert(converter = AttributeEncryptor.class) private String password;
    @OneToMany(cascade = CascadeType.ALL) @LazyCollection(LazyCollectionOption.FALSE)
    private List<SessionToken> sessionTokens = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL) @LazyCollection(LazyCollectionOption.FALSE)
    private List<ResetPasswordToken> resetPasswordTokens = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.DETACH) @LazyCollection(LazyCollectionOption.FALSE)
    private List<Role> roles = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.DETACH) @LazyCollection(LazyCollectionOption.FALSE)
    private List<Scope> scopes = new ArrayList<>();

    public User(String username, String email, String password, List<Role> roles,
        List<Scope> scopes) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.scopes = scopes;
    }

    public User(String id, String username, String email, String password, List<Role> roles,
        List<Scope> scopes) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.scopes = scopes;
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


class UserListener {
    @PrePersist @PreUpdate public void preSave(User user) {
        user.hash();
    }
}
