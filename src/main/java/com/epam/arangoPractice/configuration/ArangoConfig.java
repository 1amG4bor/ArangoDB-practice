package com.epam.arangoPractice.configuration;

import com.arangodb.*;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.EdgeEntity;
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
    private Collection<CollectionEntity> collection;
    private Collection<EdgeEntity> edgeCollection;
    @Value("${arangodb.dbname}")
    private String dbName;

    @PostConstruct
    private void initIt() {
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

    public ArangoDB getArango() {
        return arango;
    }
    public ArangoDatabase getDb() {
        return db;
    }
    public Collection<CollectionEntity> getCollection() {
        return collection;
    }
    public String getDbName() {
        return dbName;
    }

    private void loadCollections() {
        collection = db.getCollections();
        List<String> namesOfColl = collection.stream().map(CollectionEntity::getName).collect(Collectors.toList());
        log.info("The loaded collection are: {}", namesOfColl.toString());
    }

    public ArangoCollection getCollection(String name) {
            try {
                arango.db(dbName).createCollection(name);
                log.info("'{}' collection has created!", name);
            } catch (ArangoDBException e) {
                if (e.getErrorNum() == 1207)
                    log.debug("Failed to create collection '{}' in database '{}', because that name is exist now! Error: {}", name, dbName, e.getMessage());
            }
        loadCollections();
        return db.collection(name);
    }
}
