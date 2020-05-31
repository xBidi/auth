package com.spring.server.service;

import com.spring.server.model.entity.VerifyEmailToken;
import com.spring.server.repository.VerifyEmailTokenRepository;
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
@Service @Slf4j public class VerifyEmailTokenService {


    @Autowired VerifyEmailTokenRepository verifyEmailTokenRepository;
    @Autowired private UserServiceImpl userServiceImpl;

    public VerifyEmailToken generateToken() {
        String randomString = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        Timestamp expeditionDate = new Timestamp(System.currentTimeMillis());
        Timestamp expirationDate = this.getExpirationDate();
        VerifyEmailToken verifyEmailToken =
            new VerifyEmailToken(randomString, expeditionDate, expirationDate);
        verifyEmailTokenRepository.save(verifyEmailToken);
        return verifyEmailToken;
    }

    public VerifyEmailToken findByToken(String tokenString) {
        Optional<VerifyEmailToken> optionalToken =
            this.verifyEmailTokenRepository.findByToken(tokenString);
        if (!optionalToken.isPresent()) {
            return null;
        }
        return optionalToken.get();
    }

    public boolean isValid(VerifyEmailToken resetPasswordToken) {
        if (resetPasswordToken.getExpirationDate().getTime() < System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeToken(VerifyEmailToken resetPasswordToken) {
        this.userServiceImpl.removeVerifyEmailToken(resetPasswordToken.getToken());
        this.verifyEmailTokenRepository.deleteById(resetPasswordToken.getId());
    }

    public void removeToken(String tokenString) {
        VerifyEmailToken resetPasswordToken = this.findByToken(tokenString);
        this.removeToken(resetPasswordToken);
    }

    public void refreshToken(String tokenString) {
        VerifyEmailToken resetPasswordToken = this.findByToken(tokenString);
        resetPasswordToken.setExpirationDate(this.getExpirationDate());
        this.verifyEmailTokenRepository.save(resetPasswordToken);
    }

    private Timestamp getExpirationDate() {
        return new Timestamp(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
    }
}
