package com.example.demo.service;

import com.example.demo.model.entity.Token;
import com.example.demo.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
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
@Service @Slf4j public class TokenService {


    @Autowired TokenRepository tokenRepository;
    @Autowired private UserService userService;


    public Token generateToken() {
        log.debug("{generateToken start}");
        String randomString = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        Timestamp expeditionDate = new Timestamp(System.currentTimeMillis());
        Timestamp expirationDate = this.getExpirationDate();
        Token token = new Token(randomString, expeditionDate, expirationDate);
        tokenRepository.save(token);
        log.debug("{generateToken end}");
        return token;
    }

    public Token findByToken(String tokenString) throws Exception {
        log.debug("{findByToken start}");
        Optional<Token> optionalToken = this.tokenRepository.findByToken(tokenString);
        if (!optionalToken.isPresent()) {
            log.debug("{findByToken end} token not found with id: " + tokenString);
            throw new Exception("Token not found with id: " + tokenString);
        }
        Token token = optionalToken.get();
        checkToken(token);
        log.debug("{findByToken end}");
        return token;
    }

    public void checkToken(Token token) throws Exception {
        log.debug("{checkToken start}");
        if (token.getExpirationDate().getTime() < System.currentTimeMillis()) {
            this.removeToken(token);
            log.debug("{checkToken start} Token expired: " + token.getToken());
            throw new Exception("Token expired: " + token.getToken());
        }
        log.debug("{checkToken end}");
    }

    public void removeToken(Token token) throws Exception {
        log.debug("{removeToken start} (token):" + token.toString());
        this.userService.removeToken(token.getToken());
        this.tokenRepository.deleteById(token.getToken());
        log.debug("{removeToken end}");
    }

    public void removeToken(String tokenString) throws Exception {
        log.debug("{removeToken start} (tokenString):" + tokenString);
        this.removeToken(this.findByToken(tokenString));
        log.debug("{removeToken end}");
    }


    public void refreshToken(String tokenString) throws Exception {
        log.debug("{refreshToken start} (tokenString):" + tokenString);
        Token token = this.findByToken(tokenString);
        token.setExpirationDate(this.getExpirationDate());
        this.tokenRepository.save(token);
        log.debug("{refreshToken end}");
    }

    private Timestamp getExpirationDate() {
        log.debug("{getExpirationDate start}");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
        log.debug("{getExpirationDate end}");
        return timestamp;
    }
}
