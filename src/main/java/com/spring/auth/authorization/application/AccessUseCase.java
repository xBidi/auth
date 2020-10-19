package com.spring.auth.authorization.application;

import com.spring.auth.anotations.components.UseCase;
import com.spring.auth.authorization.application.ports.AccessPort;
import com.spring.auth.events.ports.PublishSessionUsedEventPort;
import com.spring.auth.exceptions.application.InvalidTokenException;
import com.spring.auth.exceptions.application.LockedUserException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.role.domain.Role;
import com.spring.auth.scope.domain.Scope;
import com.spring.auth.session.domain.Session;
import com.spring.auth.session.infrastructure.repositories.ports.DeleteSessionPort;
import com.spring.auth.session.infrastructure.repositories.ports.FindSessionPort;
import com.spring.auth.session.infrastructure.repositories.ports.RefreshSessionPort;
import com.spring.auth.user.domain.User;
import com.spring.auth.user.infrastructure.repositories.ports.FindUserPort;
import com.spring.auth.util.TokenUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author diegotobalina created on 24/06/2020
 */
@UseCase
public class AccessUseCase implements AccessPort {

    @Value("${server.auth.secret-key}")
    private String secretKey;

    @Value(("${server.auth.secret-expiration}"))
    private long expiration;

    private FindUserPort findUserPort;
    private FindSessionPort findSessionPort;
    private DeleteSessionPort deleteSessionPort;
    private PublishSessionUsedEventPort publishSessionUsedEventPort;

    public AccessUseCase(
            FindUserPort findUserPort,
            FindSessionPort findSessionPort,
            RefreshSessionPort refreshSessionPort,
            DeleteSessionPort deleteSessionPort,
            PublishSessionUsedEventPort publishSessionUsedEventPort) {
        this.findUserPort = findUserPort;
        this.findSessionPort = findSessionPort;
        this.deleteSessionPort = deleteSessionPort;
        this.publishSessionUsedEventPort = publishSessionUsedEventPort;
    }

    @Override
    public TokenUtil.JwtWrapper access(
            String token, List<String> roleValues, List<String> scopeValues)
            throws NotFoundException, InvalidTokenException, LockedUserException {
        // findAll session that will ve used for the jwt
        Session session = findSessionPort.findByToken(token);
        // if is not valid should be removed
        checkValidSession(session);
        // need the user data for the jwt generation
        User user = findUserPort.findById(session.getUserId());
        // check if the user is locked
        if (user.isLocked()) throw new LockedUserException("this user is locked");
        // session used event
        publishSessionUsedEventPort.publish(session);
        // jwt generation
        return TokenUtil.generateBearerJwt(user, secretKey, expiration, roleValues, scopeValues);
    }

    @Override
    public TokenUtil.JwtWrapper access(String userId)
            throws NotFoundException, InvalidTokenException, LockedUserException {
        User user = findUserPort.findById(userId);
        // check if the user is locked
        if (user.isLocked()) throw new LockedUserException("this user is locked");

        List<String> roles = user.getRoles().stream().map(Role::getValue).collect(Collectors.toList());
        List<String> scopes =
                user.getScopes().stream().map(Scope::getValue).collect(Collectors.toList());

        // jwt generation
        return TokenUtil.generateBearerJwt(user, secretKey, expiration, roles, scopes);
    }

    private void checkValidSession(Session session) throws InvalidTokenException {
        if (session.isValid()) return;

        deleteSessionPort.delete(session);
        throw new InvalidTokenException("token not valid");
    }
}
