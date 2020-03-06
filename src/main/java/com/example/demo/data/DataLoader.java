package com.example.demo.data;

import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Scope;
import com.example.demo.model.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.ScopeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component public class DataLoader implements ApplicationRunner {
    @Autowired RoleRepository roleRepository;
    @Autowired ScopeRepository scopeRepository;
    @Autowired UserRepository userRepository;

    public void run(ApplicationArguments args) {
        if (userRepository.findAll().size() > 0){
            return;
        }
        Role roleUser = new Role("user", "generic role", "ROLE_USER");
        Role roleAdmin = new Role("admin", "admin role", "ROLE_ADMIN");
        Scope readScope = new Scope("read", "read scope", "READ");
        Scope createScope = new Scope("create", "create scope", "CREATE");
        Scope modifyScope = new Scope("modify", "modify scope", "MODIFY");
        Scope deleteScope = new Scope("delete", "delete scope", "DELETE");

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        scopeRepository.save(readScope);
        scopeRepository.save(createScope);
        scopeRepository.save(modifyScope);
        scopeRepository.save(deleteScope);

        List<Role> rolesAdmin = new ArrayList<>();
        rolesAdmin.add(roleUser);
        rolesAdmin.add(roleAdmin);
        List<Scope> scopesAdmin = new ArrayList<>();
        scopesAdmin.add(readScope);
        scopesAdmin.add(createScope);
        scopesAdmin.add(modifyScope);
        scopesAdmin.add(deleteScope);

        User userAdmin = new User("admin", "password", rolesAdmin, scopesAdmin);

        List<Role> rolesUser = new ArrayList<>();
        rolesUser.add(roleUser);
        List<Scope> scopesUser = new ArrayList<>();
        scopesUser.add(readScope);

        User userUser = new User("user", "password", rolesUser, scopesUser);

        userRepository.save(userAdmin);
        userRepository.save(userUser);
    }
}

