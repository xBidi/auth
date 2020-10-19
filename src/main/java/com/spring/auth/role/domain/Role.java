package com.spring.auth.role.domain;

import com.spring.auth.scope.domain.Scope;
import com.spring.auth.shared.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/** @author diegotobalina created on 24/06/2020 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Role extends Auditable implements Serializable {

  private String id;
  private String name;
  private String description;
  private String value;
  private List<Scope> scopes = new ArrayList<>();

  public Role(
      Date createdAt,
      Date lastModified,
      String createdBy,
      String lastModifiedBy,
      String id,
      String name,
      String description,
      String value,
      List<Scope> scopes) {
    super(createdAt, lastModified, createdBy, lastModifiedBy);
    this.id = id;
    this.name = name;
    this.description = description;
    this.value = value;
    this.scopes = scopes;
  }

  public Role(final String value) {
    this.value = value;
  }

  public Role(final String name, final String description, final String value) {
    this.name = name;
    this.description = description;
    this.value = value;
  }

  public Role(
      final String name, final String description, final String value, final List<Scope> scopes) {
    this.name = name;
    this.description = description;
    this.value = value;
    this.scopes = scopes;
  }

  public void addScopes(List<Scope> scopes) {
    for (Scope scope : scopes) {
      if (!containsScope(scope, this.scopes)) {
        this.scopes.add(scope);
      }
    }
  }

  public void removeScopes(List<String> scopeIds) {
    for (String scopeid : scopeIds) {
      for (Scope s : this.scopes) {
        if (scopeid.equals(s.getId())) {
          this.getScopes().remove(s);
          break;
        }
      }
    }
  }

  private boolean containsScope(Scope scope, List<Scope> scopes) {
    Optional<Scope> optional =
        scopes.stream().filter(s -> s.getId().equals(scope.getId())).findFirst();
    return optional.isPresent();
  }

  public void updateScopes(List<Scope> scopes) {
    this.scopes = scopes;
  }

}
