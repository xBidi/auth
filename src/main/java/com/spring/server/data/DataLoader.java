package com.spring.server.data;

import com.spring.server.model.entity.Role;
import com.spring.server.model.entity.Scope;
import com.spring.server.model.entity.User;
import com.spring.server.repository.RoleRepository;
import com.spring.server.repository.ScopeRepository;
import com.spring.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Initial database status
 *
 * @author diegotobalina
 */
@Component @Slf4j public class DataLoader implements ApplicationRunner {

    @Autowired RoleRepository roleRepository;
    @Autowired ScopeRepository scopeRepository;
    @Autowired UserRepository userRepository;

    public void run(ApplicationArguments args) {
        if (!isDatabaseEmpty())
            return;

        // generando roles
        final Role roleUser = new Role("user", "generic role", "ROLE_USER");
        final Role roleAdmin = new Role("admin", "admin role", "ROLE_ADMIN");
        this.roleRepository.save(roleUser);
        this.roleRepository.save(roleAdmin);

        // generando scopes
        final Scope readScope = new Scope("read", "read scope", "READ");
        final Scope createScope = new Scope("create", "create scope", "CREATE");
        final Scope modifyScope = new Scope("modify", "modify scope", "MODIFY");
        final Scope deleteScope = new Scope("delete", "delete scope", "DELETE");
        final Scope readUserScope = new Scope("read_user", "read user scope", "READ_USER");
        final Scope createUserScope = new Scope("create_user", "create user scope", "CREATE_USER");
        final Scope modifyUserScope = new Scope("modify_user", "modify user scope", "MODIFY_USER");
        final Scope deleteUserScope = new Scope("delete_user", "delete user scope", "DELETE_USER");
        this.scopeRepository.save(readScope);
        this.scopeRepository.save(createScope);
        this.scopeRepository.save(modifyScope);
        this.scopeRepository.save(deleteScope);
        this.scopeRepository.save(readUserScope);
        this.scopeRepository.save(createUserScope);
        this.scopeRepository.save(modifyUserScope);
        this.scopeRepository.save(deleteUserScope);

        // admin
        final String adminUsername = "admin";
        final String adminEmail = "admin@admin.com";
        final String adminPassword = "password";
        final Boolean adminEmailVerified = false;
        final List<Role> rolesAdmin = Arrays.asList(roleUser, roleAdmin);
        final List<Scope> scopesAdmin =
            Arrays.asList(readScope, createScope, modifyScope, deleteScope);
        final User userAdmin =
            new User(adminUsername, adminEmail, adminPassword, adminEmailVerified, rolesAdmin,
                scopesAdmin);
        this.userRepository.save(userAdmin);

        // usuario
        final String userUsername = "user";
        final String userEmail = "user@user.com";
        final String userPassword = "password";
        final Boolean userEmailVerified = false;
        final List<Role> rolesUser = Arrays.asList(roleUser);
        final List<Scope> scopesUser =
            Arrays.asList(readUserScope, modifyUserScope, deleteUserScope, createUserScope);
        final User userUser =
            new User(userUsername, userEmail, userPassword, userEmailVerified, rolesUser,
                scopesUser);
        this.userRepository.save(userUser);
    }

    private boolean isDatabaseEmpty() {
        if (!this.userRepository.findAll().isEmpty())
            return false;
        if (!this.roleRepository.findAll().isEmpty())
            return false;
        if (!this.scopeRepository.findAll().isEmpty())
            return false;
        return true;
    }
}

