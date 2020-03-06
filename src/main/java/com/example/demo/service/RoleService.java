package com.example.demo.service;

import com.example.demo.model.entity.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Role functions
 *
 * @author diegotobalina
 */
@Service public class RoleService {

    @Autowired RoleRepository roleRepository;

    public Role findByValue(String value) throws Exception {
        Optional<Role> optionalRole = this.roleRepository.findByValue(value);
        if (!optionalRole.isPresent()) {
            throw new Exception("Invalid role value: " + value);
        }
        return optionalRole.get();
    }
}
