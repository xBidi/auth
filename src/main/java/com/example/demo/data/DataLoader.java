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
        log.debug("{start check database status}");
        if (userRepository.findAll().size() > 0) {
            log.debug("{database already initialized}");
            return;
        }
        log.debug("{start database initialize}");
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
        log.debug("{role saved} (role):" + roleUser.toString());
        roleRepository.save(roleAdmin);
        log.debug("{role saved} (role):" + roleAdmin.toString());
        scopeRepository.save(readScope);
        log.debug("{scope saved} (scope):" + readScope.toString());
        scopeRepository.save(createScope);
        log.debug("{scope saved} (scope):" + createScope.toString());
        scopeRepository.save(modifyScope);
        log.debug("{scope saved} (scope):" + modifyScope.toString());
        scopeRepository.save(deleteScope);
        log.debug("{scope saved} (scope):" + deleteScope.toString());

        scopeRepository.save(readUserScope);
        log.debug("{scope saved} (scope):" + readUserScope.toString());
        scopeRepository.save(createUserScope);
        log.debug("{scope saved} (scope):" + createUserScope.toString());
        scopeRepository.save(modifyUserScope);
        log.debug("{scope saved} (scope):" + modifyUserScope.toString());
        scopeRepository.save(deleteUserScope);
        log.debug("{scope saved} (scope):" + deleteUserScope.toString());

        List<Role> rolesAdmin = new ArrayList<>();
        rolesAdmin.add(roleUser);
        rolesAdmin.add(roleAdmin);
        List<Scope> scopesAdmin = new ArrayList<>();
        scopesAdmin.add(readScope);
        scopesAdmin.add(createScope);
        scopesAdmin.add(modifyScope);
        scopesAdmin.add(deleteScope);

        User userAdmin = new User("admin", "admin@admin.com", "password",false, rolesAdmin, scopesAdmin);

        List<Role> rolesUser = new ArrayList<>();
        rolesUser.add(roleUser);
        List<Scope> scopesUser = new ArrayList<>();
        scopesUser.add(readUserScope);
        scopesUser.add(modifyUserScope);
        scopesUser.add(deleteUserScope);
        scopesUser.add(createUserScope);


        User userUser = new User("user", "user@user.com", "password",false, rolesUser, scopesUser);

        userRepository.save(userAdmin);
        log.debug("{user saved} (user):" + userAdmin.toString());
        userRepository.save(userUser);
        log.debug("{user saved} (user):" + userUser.toString());
        log.debug("{end database initialize}");
    }
}

