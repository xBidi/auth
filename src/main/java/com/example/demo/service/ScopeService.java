package com.example.demo.service;

import com.example.demo.model.entity.Scope;
import com.example.demo.repository.ScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service public class ScopeService {

    @Autowired ScopeRepository ScopeRepository;

    public Scope findByValue(String value) throws Exception {
        Optional<Scope> optionalScope = this.ScopeRepository.findByValue(value);
        if (!optionalScope.isPresent()) {
            throw new Exception("Invalid Scope value: " + value);
        }
        return optionalScope.get();
    }
}
