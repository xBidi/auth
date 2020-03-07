package com.example.demo.service;

import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Scope;
import com.example.demo.model.entity.Token;
import com.example.demo.model.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Authentication functions
 *
 * @author diegotobalina
 */
@Service @Slf4j public class AuthService {

    @Autowired UserService userService;
    @Autowired TokenService tokenService;
    @Value("${server.auth.secret-key}") private String secretKey;

    @Transactional(rollbackOn = Exception.class)
    public LoginOutputDto login(LoginInputDto loginInputDto) throws Exception {
        log.debug("{login start}");
        User user = this.userService.findByUsername(loginInputDto.getUsername());
        String inputPassword = loginInputDto.getPassword();
        String userPasswordHash = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(inputPassword, userPasswordHash)) {
            throw new Exception("Invalid password");
        }
        Token token = this.tokenService.generateToken();
        this.userService.addToken(user, token);
        LoginOutputDto loginOutputDto =
            new LoginOutputDto(token.getToken(), token.getExpeditionDate().toString(),
                token.getExpirationDate().toString());
        log.debug("{login end}");
        return loginOutputDto;
    }

    @Transactional(rollbackOn = Exception.class)
    public AccessOutputDto access(AccessInputDto accessInputDto) throws Exception {
        log.debug("{access start}");
        String tokenString = accessInputDto.getToken();
        tokenString = tokenString.replace("Bearer ", "");
        tokenService.findByToken(tokenString);
        tokenService.refreshToken(tokenString);
        User user = userService.findByToken(tokenString);
        String username = user.getUsername();

        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis + (15 * 60 * 1000);
        Timestamp expeditionDate = new Timestamp(currentTimeMillis);
        Timestamp expirationDate = new Timestamp(expirationTimeMillis);
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user.getId());
        claims.put("scopes", user.getScopes());
        claims.put("roles", user.getRoles());
        String token = Jwts.builder().setId("").setSubject(username).setClaims(claims)
            .setIssuedAt(new Date(currentTimeMillis)).setExpiration(new Date(expirationTimeMillis))
            .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
        AccessOutputDto accessOutputDto =
            new AccessOutputDto(token, expeditionDate.toString(), expirationDate.toString());
        log.debug("{access end}");
        return accessOutputDto;
    }

    public void logout(LogoutInputDto logoutInputDto) throws Exception {
        log.debug("{logout start}");
        String token = logoutInputDto.getToken();
        token = token.replace("Bearer ", "");
        this.tokenService.removeToken(token);
        log.debug("{logout end}");
    }


    public User validateJwt(String token) {
        log.debug("{validateJwt start}");
        Claims claims = this.validateToken(token);
        ObjectMapper mapper = new ObjectMapper();
        List<Role> roles =
            mapper.convertValue(claims.get("roles"), new TypeReference<List<Role>>() {
            });
        List<Scope> scopes =
            mapper.convertValue(claims.get("scopes"), new TypeReference<List<Scope>>() {
            });
        String userString = (String) claims.get("user");
        User user = new User(userString, "", "", roles, scopes);
        log.debug("{validateJwt end}");
        return user;
    }

    private Claims validateToken(String token) {
        log.debug("{validateToken start}");
        Claims claims =
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        log.debug("{validateToken end}");
        return claims;
    }

    public TokenInfoOutputDto tokenInfo(TokenInfoInputDto tokenInfoInputDto) throws Exception {
        log.debug("{tokenInfo start}");
        String tokenString = tokenInfoInputDto.getToken();
        tokenString = tokenString.replace("Bearer ", "");
        String regex = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tokenString);
        if (!m.matches()) {
            log.debug("{tokenInfo} start token validation");
            User user = userService.findByToken(tokenString);
            Token token = tokenService.findByToken(tokenString);
            TokenInfoOutputDto tokenInfoOutputDto =
                new TokenInfoOutputDto(tokenString, token.getExpeditionDate().toString(),
                    token.getExpirationDate().toString(), user.getId());
            log.debug("{tokenInfo} end token validation");
            log.debug("{tokenInfo end} token");
            return tokenInfoOutputDto;
        } else {
            log.debug("{tokenInfo} start jwt validation");
            Claims claims = this.validateToken(tokenString);
            Integer expeditionDate = (Integer) claims.get("iat");
            Integer expirationDate = (Integer) claims.get("exp");
            User user = this.validateJwt(tokenString);
            List<String> roles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roles.add(role.getValue());
            }
            List<String> scopes = new ArrayList<>();
            for (Scope scope : user.getScopes()) {
                scopes.add(scope.getValue());
            }
            TokenInfoJwtOutputDto tokenInfoJwtOutputDto =
                new TokenInfoJwtOutputDto(tokenString, new Timestamp(expeditionDate).toString(),
                    new Timestamp(expirationDate).toString(), user.getId(), roles, scopes);
            log.debug("{tokenInfo} end jwt validation");
            log.debug("{tokenInfo end} jwt");
            return tokenInfoJwtOutputDto;
        }
    }

    @Transactional public UserInfoOutputDto findByPrincipal(Principal principal) throws Exception {
        log.debug("{findByPrincipal start}");
        UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken) principal;
        User tempUser = (User) authenticationToken.getPrincipal();
        UserInfoOutputDto userInfoOutputDto = getUserInfoOutputDto(tempUser.getId());
        log.debug("{findByPrincipal end}");
        return userInfoOutputDto;
    }

    public UserInfoOutputDto getUserInfoOutputDto(String userId) throws Exception {
        log.debug("{getUserInfoOutputDto start}");
        User user = this.userService.findById(userId);
        List<String> roles = new ArrayList<>();
        user.getRoles().stream().forEach(role -> roles.add(role.getValue()));
        List<String> scopes = new ArrayList<>();
        user.getScopes().stream().forEach(scope -> scopes.add(scope.getValue()));
        List<TokenDto> sessions = new ArrayList<>();
        user.getTokens().stream().forEach(token -> {
            try {
                this.tokenService.checkToken(token);
                sessions.add(new TokenDto(token.getToken(), token.getExpeditionDate().toString(),
                    token.getExpirationDate().toString()));
            } catch (Exception ex) {
                log.warn(
                    "{getUserInfoOutputDto} (this.tokenService.checkToken):" + ex.getMessage());
            }
        });
        log.debug("{getUserInfoOutputDto end}");
        return new UserInfoOutputDto(user.getId(), user.getUsername(), roles, scopes, sessions);
    }
}
