package com.example.demo.service;

import com.example.demo.model.dto.*;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Scope;
import com.example.demo.model.entity.SessionToken;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Authentication functions
 *
 * @author diegotobalina
 */
@Service @Slf4j public class AuthService {

    @Autowired UserService userService;
    @Autowired SessionTokenService sessionTokenService;
    @Value("${server.auth.secret-key}") private String secretKey;
    @Value("${google.oauth2.CLIENT_ID}") private String googleClientId;

    public LoginOutputDto login(LoginInputDto loginInputDto) throws Exception {
        log.debug("{login start}");
        String username = loginInputDto.getUsername();
        String email = loginInputDto.getEmail();
        String inputPassword = loginInputDto.getPassword();
        User user = userService.findByUsernameOrEmail(username, email);
        String userPasswordHash = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(inputPassword, userPasswordHash)) {
            throw new Exception("invalid password");
        }
        SessionToken sessionToken = this.sessionTokenService.generateToken();
        userService.addSessionToken(user, sessionToken);
        return new LoginOutputDto(sessionToken.getToken(),
            sessionToken.getExpeditionDate().toString(),
            sessionToken.getExpirationDate().toString());
    }

    public AccessOutputDto access(AccessInputDto accessInputDto) throws Exception {
        String tokenString = accessInputDto.getToken();
        tokenString = tokenString.replace("Bearer ", "");
        SessionToken sessionToken = sessionTokenService.findByToken(tokenString);
        if (!sessionTokenService.isValid(sessionToken)) {
            sessionTokenService.removeToken(sessionToken);
            throw new Exception("invalid token");
        }
        sessionTokenService.refreshToken(tokenString);
        User user = userService.findBySessionTokensToken(tokenString);
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
        return new AccessOutputDto(token, expeditionDate.toString(), expirationDate.toString());
    }

    public void logout(LogoutInputDto logoutInputDto) {
        String token = logoutInputDto.getToken();
        token = token.replace("Bearer ", "");
        sessionTokenService.removeToken(token);
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
        return new User(userString, "", "", "", false, roles, scopes);
    }

    private Claims validateToken(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }

    public TokenInfoOutputDto tokenInfo(String tokenString) throws Exception {
        tokenString = tokenString.replace("Bearer ", "");
        String regex = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tokenString);
        if (!m.matches()) { // token de login
            return getSessionTokenInfo(tokenString);
        }
        if (m.matches()) { // jwt
            return getJwtTokenInfo(tokenString);
        }
        throw new Exception("unknow jwt format");
    }

    public TokenInfoJwtOutputDto getJwtTokenInfo(String tokenString) throws Exception {
        // validate jwt
        TokenInfoJwtOutputDto tokenInfoOutputDto;
        if ((tokenInfoOutputDto = getJwtInfo(tokenString)) != null) {
            return tokenInfoOutputDto;
        }
        if ((tokenInfoOutputDto = getGoogleJwtInfo(tokenString)) != null) {
            return tokenInfoOutputDto;
        }
        throw new Exception("failed to get token info");
    }

    private TokenInfoJwtOutputDto getJwtInfo(String tokenString) {
        try {
            Claims claims = this.validateToken(tokenString);
            Integer expeditionDate = (Integer) claims.get("iat");
            Integer expirationDate = (Integer) claims.get("exp");
            User user = this.validateJwt(tokenString);
            return userToTokenInfoOutputDto(tokenString, expeditionDate, expirationDate, user);
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            return null;
        }
    }

    private TokenInfoJwtOutputDto getGoogleJwtInfo(String tokenString) {
        try {
            GoogleIdToken.Payload googleInfo = getGoogleInfo(tokenString);
            if (googleInfo == null) {
                throw new Exception("failed google login");
            }
            Integer expeditionDate = Math.toIntExact(googleInfo.getIssuedAtTimeSeconds());
            Integer expirationDate = Math.toIntExact(googleInfo.getExpirationTimeSeconds());
            User user = googleLogin(googleInfo);
            return userToTokenInfoOutputDto(tokenString, expeditionDate, expirationDate, user);
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            return null;
        }
    }

    private TokenInfoJwtOutputDto userToTokenInfoOutputDto(String tokenString,
        Integer expeditionDate, Integer expirationDate, User user) {
        List<String> roles =
            user.getRoles().stream().map(role -> role.getValue()).collect(Collectors.toList());
        List<String> scopes =
            user.getScopes().stream().map(scope -> scope.getValue()).collect(Collectors.toList());
        return new TokenInfoJwtOutputDto(tokenString, new Timestamp(expeditionDate).toString(),
            new Timestamp(expirationDate).toString(), user.getId(), roles, scopes);
    }

    private TokenInfoOutputDto getSessionTokenInfo(String tokenString) {
        User user = userService.findBySessionTokensToken(tokenString);
        SessionToken sessionToken = sessionTokenService.findByToken(tokenString);
        return new TokenInfoOutputDto(tokenString, sessionToken.getExpeditionDate().toString(),
            sessionToken.getExpirationDate().toString(), user.getId());
    }

    public UserInfoOutputDto findByPrincipal(Principal principal) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken) principal;
        User tempUser = (User) authenticationToken.getPrincipal();
        User user = userService.findById(tempUser.getId());
        UserInfoOutputDto userInfoOutputDto = userService.userToUserInfoOutputDto(user);
        return userInfoOutputDto;
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
            .setAudience(Collections.singletonList(googleClientId)).build();
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            return null;
        }
    }

    public User googleLogin(GoogleIdToken.Payload payload) throws Exception {
        String email = payload.getEmail();
        String username = email.split("@")[0] + UUID.randomUUID().toString().substring(0, 4);
        String randomPassword = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        Boolean emailVerified = payload.getEmailVerified();
        if (userService.findByEmail(email) != null) {
            return userService.findByEmail(email);
        }
        User user =
            new User(null, username, email, randomPassword, emailVerified, new ArrayList<>(),
                new ArrayList<>());
        return userService.createUser(user);
    }
}
