package com.epam.arangoPractice.configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.CollectionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class ArangoConfig {
    private final ArangoDB arango = new ArangoDB.Builder().build();
    private ArangoDatabase db;
    private Collection<CollectionEntity> collections;

    @Value("${arangodb.dbname}")
    private String dbName;

    @PostConstruct
    private void initIt() {
        arango.db(dbName).drop();
        try {
            arango.createDatabase(dbName);
            log.debug("'{}' database has created", dbName);
        } catch (ArangoDBException e) {
            if (e.getErrorNum()==1207) {
                log.debug("Failed to create database: '{}', because that name is exist now. Error: " + e.getMessage());
            }
        }
        db = arango.db(dbName);
        loadCollections();
    }

    private void loadCollections() {
        collections = db.getCollections();
        List<String> namesOfColl = collections.stream().map(CollectionEntity::getName).collect(Collectors.toList());
        log.info("The loaded collections are: {}", namesOfColl.toString());
    }

    public boolean createCollection(String name) {
        try {
            arango.db(dbName).createCollection(name);
            log.info("'{}' collection has created", name);
        } catch (ArangoDBException e) {
            if (e.getErrorNum()==1207) log.debug("Failed to create collection '{}' in database '{}', because that name is exist now. Error: {}", name, dbName, e.getMessage());
            return false;
        }
        loadCollections();
        return true;
    }

    public ArangoDB getArango() {
        return arango;
    }

    public ArangoDatabase getDb() {
        return db;
    }

    public Collection<CollectionEntity> getCollections() {
        return collections;
    }

    public String getDbName() {
        return dbName;
    }
}
