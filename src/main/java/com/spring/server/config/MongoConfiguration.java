package com.spring.server.config;

import io.github.kaiso.relmongo.config.EnableRelMongo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableRelMongo
public class MongoConfiguration {

  private final MongoTemplate mongoTemplate;

  private final MongoConverter mongoConverter;

  @Bean
  @Autowired
  @ConditionalOnExpression("'${mongo.transactions}'=='enabled'")
  MongoTransactionManager mongoTransactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initIndicesAfterStartup() {
    var mappingContext = getMappingContext();
    if (!(mappingContext instanceof MongoMappingContext)) {
      return;
    }
    MongoMappingContext mongoMappingContext = (MongoMappingContext) mappingContext;
    var persistentEntities = mongoMappingContext.getPersistentEntities();
    persistentEntities.forEach(
        persistentEntity -> {
          var persistentEntityType = persistentEntity.getType();
          if (persistentEntityType.isAnnotationPresent(Document.class)) {
            var resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
            IndexOperations indexOps = mongoTemplate.indexOps(persistentEntityType);
            resolver.resolveIndexFor(persistentEntityType).forEach(indexOps::ensureIndex);
          }
        });
  }

  private MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty>
      getMappingContext() {
    return this.mongoConverter.getMappingContext();
  }
}
