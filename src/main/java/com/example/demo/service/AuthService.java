package com.example.demo.service;

import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Scope;
import com.example.demo.model.entity.Token;
import com.example.demo.model.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
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
    @Value("${google.oauth2.CLIENT_ID}") private String googleClientId;

    @Transactional(rollbackOn = Exception.class)
    public LoginOutputDto login(LoginInputDto loginInputDto) throws Exception {
        log.debug("{login start}");
        User user;
        if (!loginInputDto.getUsername().isEmpty()) {
            user = this.userService.findByUsername(loginInputDto.getUsername());
        } else {
            user = this.userService.findByEmail(loginInputDto.getEmail());
        }
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
        User user = new User(userString, "", "", "", roles, scopes);
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
        if (!m.matches()) { // token de login
            log.debug("{tokenInfo} start token validation");
            User user = userService.findByToken(tokenString);
            Token token = tokenService.findByToken(tokenString);
            TokenInfoOutputDto tokenInfoOutputDto =
                new TokenInfoOutputDto(tokenString, token.getExpeditionDate().toString(),
                    token.getExpirationDate().toString(), user.getId());
            log.debug("{tokenInfo} end token validation");
            log.debug("{tokenInfo end} token");
            return tokenInfoOutputDto;
        }
        if (m.matches()) {
            log.debug("{tokenInfo} start jwt validation");
            Integer expeditionDate;
            Integer expirationDate;
            User user;
            try { // TODO: need refactor
                Claims claims = this.validateToken(tokenString);
                expeditionDate = (Integer) claims.get("iat");
                expirationDate = (Integer) claims.get("exp");
                user = this.validateJwt(tokenString);
            } catch (Exception ex) {
                GoogleIdToken.Payload googleInfo = getGoogleInfo(tokenString);
                if (googleInfo == null) {
                    throw new Exception("failed google login");
                }
                expeditionDate = Math.toIntExact(googleInfo.getIssuedAtTimeSeconds());
                expirationDate = Math.toIntExact(googleInfo.getExpirationTimeSeconds());
                user = googleLogin(googleInfo);
            }

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
        throw new Exception("unknow jwt format");
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
        return new UserInfoOutputDto(user.getId(), user.getUsername(), user.getEmail(), roles,
            scopes, sessions);
    }

    private NetHttpTransport transport;
    private JacksonFactory jsonFactory;

    public GoogleIdToken.Payload getGoogleInfo(String token)
        throws GeneralSecurityException, IOException {
        if (transport == null && jsonFactory == null) {
            transport = GoogleNetHttpTransport.newTrustedTransport();
            jsonFactory = JacksonFactory.getDefaultInstance();
        }
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            // Specify the CLIENT_ID of the app that accesses the backend:
            .setAudience(Collections.singletonList(googleClientId))
            // Or, if multiple clients access the backend:
            //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
            .build();
        // (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            return null;
        }
    }

    // remove google cache every 10 sec
    @CacheEvict(allEntries = true, cacheNames = {"google"}) @Scheduled(fixedDelay = (10000))
    public void cacheEvict() {
    }

    public User googleLogin(GoogleIdToken.Payload payload) throws Exception {
        String email = payload.getEmail();
        String username = email.split("@")[0] + UUID.randomUUID().toString().substring(0, 4);
        String randomPassword = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        User user =
            new User(null, username, email, randomPassword, new ArrayList<>(), new ArrayList<>());
        userService.createIfNotExist(user);
        user = userService.findByEmail(email);
        return user;
    }
}
