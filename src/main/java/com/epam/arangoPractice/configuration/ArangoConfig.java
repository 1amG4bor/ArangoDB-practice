package com.epam.arangoPractice.configuration;

import com.arangodb.*;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.EdgeDefinition;
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
    @Value("${arangodb.dbName}")
    private String dbName;
    @Value("${arangodb.graphName}")
    private String graphName;

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

    public String getGraphName() {
        return graphName;
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

    public ArangoEdgeCollection getEdgeCollection(String name) {
        ArangoEdgeCollection result = null;
        try {
            result = arango.db(dbName).graph(graphName).edgeCollection(name);
            log.info("'{}' edge-collection has created!", name);
        } catch (ArangoDBException e) {
            log.error(e.getErrorMessage());
        }
        return result;
    }

    public ArangoGraph getGraph(String name, ArangoEdgeCollection edgeCollection) {
        try {
            return getDb().graph(name);
        } catch (ArangoDBException e) {
            log.error("Error: {}, message: {}", e.getErrorNum(), e.getErrorMessage());
        }
        return null;
    }
}
