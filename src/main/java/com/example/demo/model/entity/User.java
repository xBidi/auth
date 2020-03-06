package com.example.demo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity @Getter @Setter @NoArgsConstructor @EntityListeners(UserListener.class)
@Table(name = "spring_user") public class User {

    @Id @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(unique = true) private String username;
    private String password;
    @OneToMany(cascade = CascadeType.ALL) private List<Token> tokens = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.DETACH) private List<Role> roles = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.DETACH) private List<Scope> scopes = new ArrayList<>();

    public User(String username, String password, List<Role> roles, List<Scope> scopes) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.scopes = scopes;
    }

    public User(String id, String username, String password, List<Role> roles, List<Scope> scopes) {
        this.id = id;
        this.username = username;
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
