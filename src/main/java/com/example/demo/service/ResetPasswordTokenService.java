package com.example.demo.service;

import com.example.demo.model.entity.ResetPasswordToken;
import com.example.demo.repository.ResetPasswordTokenRepository;
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
@Service @Slf4j public class ResetPasswordTokenService {


    @Autowired ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Autowired private UserService userService;


    public ResetPasswordToken generateToken() {
        String randomString = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        Timestamp expeditionDate = new Timestamp(System.currentTimeMillis());
        Timestamp expirationDate = this.getExpirationDate();
        ResetPasswordToken resetPasswordToken =
            new ResetPasswordToken(randomString, expeditionDate, expirationDate);
        resetPasswordTokenRepository.save(resetPasswordToken);
        return resetPasswordToken;
    }

    public ResetPasswordToken findByToken(String tokenString) {
        Optional<ResetPasswordToken> optionalToken =
            this.resetPasswordTokenRepository.findByToken(tokenString);
        if (!optionalToken.isPresent()) {
            return null;
        }
        return optionalToken.get();
    }

    public boolean isValid(ResetPasswordToken resetPasswordToken) {
        if (resetPasswordToken.getExpirationDate().getTime() < System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    public void removeToken(ResetPasswordToken resetPasswordToken) {
        this.userService.removeResetPasswordToken(resetPasswordToken.getToken());
        this.resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());
    }

    public void removeToken(String tokenString) {
        ResetPasswordToken resetPasswordToken = this.findByToken(tokenString);
        this.removeToken(resetPasswordToken);
    }

    public void refreshToken(String tokenString) {
        ResetPasswordToken resetPasswordToken = this.findByToken(tokenString);
        resetPasswordToken.setExpirationDate(this.getExpirationDate());
        this.resetPasswordTokenRepository.save(resetPasswordToken);
    }

    private Timestamp getExpirationDate() {
        return new Timestamp(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
    }
}
