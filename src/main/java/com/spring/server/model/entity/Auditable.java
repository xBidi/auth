package com.spring.server.model.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

public abstract class Auditable<U> {
  @CreatedBy protected U createdBy;
  @CreatedDate protected Date creationDate;
  @LastModifiedBy protected U lastModifiedBy;
  @LastModifiedDate protected Date lastModifiedDate;
}
