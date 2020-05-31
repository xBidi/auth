package com.spring.server.service;

import com.spring.server.model.entity.SessionToken;
import com.spring.server.repository.SessionTokenRepository;
import com.spring.server.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

/**
 * Token functions
 *
 * @author diegotobalina
 */
@Service @Slf4j public class SessionTokenService {


    @Autowired SessionTokenRepository sessionTokenRepository;
    @Autowired private UserServiceImpl userServiceImpl;

    public SessionToken generateToken() {
        String randomString = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        Timestamp expeditionDate = new Timestamp(System.currentTimeMillis());
        Timestamp expirationDate = this.getExpirationDate();
        SessionToken sessionToken = new SessionToken(randomString, expeditionDate, expirationDate);
        sessionTokenRepository.save(sessionToken);
        return sessionToken;
    }

    public SessionToken findByToken(String tokenString) {
        Optional<SessionToken> optionalToken = this.sessionTokenRepository.findByToken(tokenString);
        if (!optionalToken.isPresent()) {
            return null;
        }
        return optionalToken.get();
    }

    public SessionToken findValidByToken(String token) throws Exception {
        final SessionToken sessionToken = this.findByToken(token);
        if (!(this.isValid(sessionToken))) {
            this.removeToken(sessionToken);
            throw new Exception("invalid token");
        }
        return sessionToken;
    }

    public boolean isValid(SessionToken sessionToken) {
        if (sessionToken.getExpiration().getTime() < System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeToken(SessionToken sessionToken) {
        this.userServiceImpl.removeSessionToken(sessionToken.getToken());
        this.sessionTokenRepository.deleteById(sessionToken.getId());
    }

    public void removeToken(String tokenString) {
        SessionToken sessionToken = this.findByToken(tokenString);
        this.removeToken(sessionToken);
    }

    public void refreshToken(String tokenString) {
        SessionToken sessionToken = this.findByToken(tokenString);
        sessionToken.setExpiration(this.getExpirationDate());
        this.sessionTokenRepository.save(sessionToken);
    }

    private Timestamp getExpirationDate() {
        return new Timestamp(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
    }
}
