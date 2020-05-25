package com.example.demo.service;

import com.example.demo.model.entity.VerifyEmailToken;
import com.example.demo.model.entity.VerifyEmailToken;
import com.example.demo.repository.VerifyEmailTokenRepository;
import com.example.demo.repository.VerifyEmailTokenRepository;
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
@Service @Slf4j public class VerifyEmailTokenService {


    @Autowired VerifyEmailTokenRepository verifyEmailTokenRepository;
    @Autowired private UserService userService;


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

    public void removeToken(VerifyEmailToken resetPasswordToken) {
        this.userService.removeVerifyEmailToken(resetPasswordToken.getToken());
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
