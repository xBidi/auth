package com.spring.auth.session.infrastructure.repositories.ports;

import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.session.domain.Session;

/** @author diegotobalina created on 24/06/2020 */
public interface CreateSessionPort {
  Session create(Session session) throws DuplicatedKeyException, NotFoundException;
}
