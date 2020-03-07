package com.example.demo.service;

import com.example.demo.model.entity.Scope;
import com.example.demo.repository.ScopeRepository;
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

    public Scope findByValue(String value) throws Exception {
        log.debug("{findByValue start}");
        Optional<Scope> optionalScope = this.ScopeRepository.findByValue(value);
        if (!optionalScope.isPresent()) {
            log.debug("{findByValue end} invalid scope value: " + value);
            throw new Exception("Invalid Scope value: " + value);
        }
        log.debug("{findByValue end}");
        return optionalScope.get();
    }
}
