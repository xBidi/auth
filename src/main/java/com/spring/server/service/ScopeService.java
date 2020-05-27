package com.spring.server.service;

import com.spring.server.model.entity.Scope;
import com.spring.server.repository.ScopeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Scope functions
 *
 * @author diegotobalina
 */
@Service @Slf4j public class ScopeService {

    @Autowired ScopeRepository ScopeRepository;

      public Scope findByValue(String value) {
        Optional<Scope> optionalScope = this.ScopeRepository.findByValue(value);
        if (!optionalScope.isPresent()) {
            return null;
        }
        return optionalScope.get();
    }
}
