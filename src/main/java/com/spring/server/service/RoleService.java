package com.spring.server.service;

import com.spring.server.model.entity.Role;
import com.spring.server.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Role functions
 *
 * @author diegotobalina
 */
@Service @Slf4j public class RoleService {

    @Autowired RoleRepository roleRepository;

    public Role findByValue(String value) {
        Optional<Role> optionalRole = this.roleRepository.findByValue(value);
        if (!optionalRole.isPresent()) {
            return null;
        }
        return optionalRole.get();
    }
}
