package com.example.demo.service;

import com.example.demo.model.entity.Role;
import com.example.demo.repository.RoleRepository;
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

    public Role findByValue(String value) throws Exception {
        log.debug("{findByValue start}");
        Optional<Role> optionalRole = this.roleRepository.findByValue(value);
        if (!optionalRole.isPresent()) {
            log.debug("{findByValue end} invalid role value: " + value);
            throw new Exception("Invalid role value: " + value);
        }
        log.debug("{findByValue end}");
        return optionalRole.get();
    }
}
