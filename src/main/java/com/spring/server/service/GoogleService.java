package com.spring.server.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.server.model.entity.User;
import com.spring.server.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@Service public class GoogleService {

    @Autowired private UserServiceImpl userServiceImpl;
    @Value("${google.oauth2.client_id}") private String googleClientId;
    private NetHttpTransport transport;
    private JacksonFactory jsonFactory;

    public GoogleIdToken.Payload getGoogleInfo(String jwt)
        throws GeneralSecurityException, IOException {
        if (transport == null && jsonFactory == null) {
            transport = GoogleNetHttpTransport.newTrustedTransport();
            jsonFactory = JacksonFactory.getDefaultInstance();
        }
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(googleClientId)).build();
        GoogleIdToken idToken = verifier.verify(jwt);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            return null;
        }
    }

    public User googleLogin(GoogleIdToken.Payload payload) throws Exception {
        final String email = payload.getEmail();
        final User userByEmail = this.userServiceImpl.findByEmail(email);
        if (userByEmail != null) {
            return userByEmail;
        }
        final String username = email.split("@")[0] + UUID.randomUUID().toString().substring(0, 4);
        final String randomPassword =
            System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        final Boolean emailVerified = payload.getEmailVerified();
        User user = new User(username, email, randomPassword, emailVerified, new ArrayList<>(),
            new ArrayList<>());
        return userServiceImpl.createUser(user);
    }
}
