package com.example.demo.data;

import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Scope;
import com.example.demo.model.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.ScopeRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

        log.info("check database status");
        if (userRepository.findAll().size() > 0) {
            log.info("database already initialized");
            return;
        }

        log.info("start database initialization");
        long currentTimeMillis = System.currentTimeMillis();

        Role roleUser = new Role("user", "generic role", "ROLE_USER");
        Role roleAdmin = new Role("admin", "admin role", "ROLE_ADMIN");
        Scope readScope = new Scope("read", "read scope", "READ");
        Scope createScope = new Scope("create", "create scope", "CREATE");
        Scope modifyScope = new Scope("modify", "modify scope", "MODIFY");
        Scope deleteScope = new Scope("delete", "delete scope", "DELETE");
        Scope readUserScope = new Scope("read_user", "read user scope", "READ_USER");
        Scope createUserScope = new Scope("create_user", "create user scope", "CREATE_USER");
        Scope modifyUserScope = new Scope("modify_user", "modify user scope", "MODIFY_USER");
        Scope deleteUserScope = new Scope("delete_user", "delete user scope", "DELETE_USER");

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);

        scopeRepository.save(readScope);
        scopeRepository.save(createScope);
        scopeRepository.save(modifyScope);
        scopeRepository.save(deleteScope);
        scopeRepository.save(readUserScope);
        scopeRepository.save(createUserScope);
        scopeRepository.save(modifyUserScope);
        scopeRepository.save(deleteUserScope);

        log.debug("saved role: " + roleUser.toString());
        log.debug("saved role: " + roleAdmin.toString());

        log.debug("saved scope: " + readScope.toString());
        log.debug("saved scope: " + createScope.toString());
        log.debug("saved scope: " + modifyScope.toString());
        log.debug("saved scope: " + deleteScope.toString());
        log.debug("saved scope: " + readUserScope.toString());
        log.debug("saved scope: " + createUserScope.toString());
        log.debug("saved scope: " + modifyUserScope.toString());
        log.debug("saved scope: " + deleteUserScope.toString());


        List<Role> rolesAdmin = Arrays.asList(roleUser, roleAdmin);
        List<Scope> scopesAdmin = Arrays.asList(readScope, createScope, modifyScope, deleteScope);

        User userAdmin =
            new User("admin", "admin@admin.com", "password", false, rolesAdmin, scopesAdmin);

        List<Role> rolesUser = Arrays.asList(roleUser);
        List<Scope> scopesUser =
            Arrays.asList(readUserScope, modifyUserScope, deleteUserScope, createUserScope);

        User userUser = new User("user", "user@user.com", "password", false, rolesUser, scopesUser);

        userRepository.save(userAdmin);
        userRepository.save(userUser);

        log.debug("saved user: " + userAdmin.toString());
        log.debug("saved user: " + userUser.toString());

        log.info("database initialized in {} ms", System.currentTimeMillis() - currentTimeMillis);
    }
}

