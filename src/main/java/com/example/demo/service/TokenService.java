package com.example.demo.service;

import com.example.demo.model.entity.Token;
import com.example.demo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

/**
 * Token functions
 *
 * @author diegotobalina
 */
@Service public class TokenService {


    @Autowired TokenRepository tokenRepository;
    @Autowired private UserService userService;


    public Token generateToken() {
        String randomString = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        Timestamp expeditionDate = new Timestamp(System.currentTimeMillis());
        Timestamp expirationDate = this.getExpirationDate();
        Token token = new Token(randomString, expeditionDate, expirationDate);
        return tokenRepository.save(token);
    }

    public Token findById(String id) throws Exception {
        Optional<Token> optionalToken = this.tokenRepository.findById(id);
        if (!optionalToken.isPresent()) {
            throw new Exception("Token not found with id: " + id);
        }
        Token token = optionalToken.get();
        checkToken(token);
        return token;
    }

    public void checkToken(Token token) throws Exception {
        if (token.getExpirationDate().getTime() < System.currentTimeMillis()) {
            this.removeToken(token);
            throw new Exception("Token expired: " + token.getId());
        }
    }

    public void removeToken(Token token) throws Exception {
        this.userService.removeToken(token.getId());
        this.tokenRepository.deleteById(token.getId());
    }

    public void removeToken(String tokenString) throws Exception {
        this.removeToken(this.findById(tokenString));
    }


    public void refreshToken(String tokenString) throws Exception {
        Token token = this.findById(tokenString);
        token.setExpirationDate(this.getExpirationDate());
        this.tokenRepository.save(token);
    }

    private Timestamp getExpirationDate() {
        return new Timestamp(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
    }
}
