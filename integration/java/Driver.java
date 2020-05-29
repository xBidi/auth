package com.spring.server.security;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Driver {

    private RestTemplate restTemplate = new RestTemplate();
    private String apiUrl;

    public Driver(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String login(String email, String username, String password) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        return send("/oauth2/login", null, HttpMethod.POST, jsonObject.toString());
    }

    public String access(String token) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return send("/oauth2/access", null, HttpMethod.POST, jsonObject.toString());
    }

    public String logout(String token) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return send("/oauth2/logout", null, HttpMethod.DELETE, jsonObject.toString());
    }

    public String tokenInfo(String token) {
        return send("/oauth2/tokenInfo?token=" + token, null, HttpMethod.GET, null);
    }

    public String userInfo(String token) {
        return send("/oauth2/userInfo", token, HttpMethod.GET, null);
    }

    public String listUsers(String token) {
        return send("/users", token, HttpMethod.GET, null);
    }

    public String sendVerificationEmail(String token) {
        return send("/users/email", token, HttpMethod.POST, null);
    }

    public String verifyEmail(String token) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return send("/users/email/verify", null, HttpMethod.POST, jsonObject.toString());
    }

    public String updateUserPassword(String token, String oldPassword, String newPassword)
        throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("oldPassword", oldPassword);
        jsonObject.put("newPassword", newPassword);
        return send("/users/password", token, HttpMethod.PUT, jsonObject.toString());
    }

    public String sendResetPasswordEmail(String token) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return send("/users/password/email", null, HttpMethod.POST, jsonObject.toString());
    }

    public String resetPasswordWithEmailToken(String token, String newPassword)
        throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("newPassword", newPassword);
        return send("/users/password/reset", null, HttpMethod.PUT, jsonObject.toString());
    }

    public String registerUser(String email, String username, String password)
        throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        return send("/users/register", null, HttpMethod.POST, jsonObject.toString());
    }

    private String send(String url, String token, HttpMethod httpMethod, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(apiUrl + url, httpMethod, request, String.class).getBody();
    }

}
