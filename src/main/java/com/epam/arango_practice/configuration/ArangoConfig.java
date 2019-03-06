package com.epam.arango_practice.configuration;

import com.arangodb.*;
import com.arangodb.entity.CollectionType;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.model.GraphCreateOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Configuration
@Slf4j
public class ArangoConfig {
	@Value("${arangodb.dbname}")
	private String dbName;
	@Value("${arangodb.graphname}")
	private String graphName;
	@Value("${arangodb.nodecollection}")
	private String collectionName;
	@Value("${arangodb.edgecollection}")
	private String edgeCollectionName;

	private final ArangoDB arango = new ArangoDB.Builder().build();
	private ArangoDatabase aDb;
	private ArangoCollection family;
	private ArangoEdgeCollection relations;

	public ArangoConfig() {
	}

	public ArangoConfig(String dbName, String collectionName, String edgeCollectionName) {
		this.dbName = dbName;
		this.collectionName = collectionName;
		this.edgeCollectionName = edgeCollectionName;
	}

	@PostConstruct
	public void initIt() {
		aDb = createDatabase(dbName);
		family = createCollection(collectionName);
		relations = createEdgeCollection(edgeCollectionName);
	}

	@Bean
	public ArangoDatabase getaDb() {
		return aDb;
	}

	public ArangoDatabase createDatabase(String name) {
		if (!arango.db(name).exists()) {
			try {
				arango.createDatabase(name);
				log.debug("'{}' database has created", name);
			} catch (ArangoDBException e) {
				log.error("Failed to create database. Error: {}" + e.getMessage());
			}
		}
		return arango.db(name);
	}

	public ArangoCollection createCollection(String name) {
		if (!aDb.collection(name).exists()) {
			try {
				aDb.createCollection(name);
				log.info("'{}' collection has created!", name);
			} catch (ArangoDBException e) {
				log.error(e.getErrorMessage());
			}
		}
		return aDb.collection(name);
	}

	public ArangoEdgeCollection createEdgeCollection(String name) {
		if (!aDb.collection(name).exists() && !aDb.createCollection(name).getType().equals(CollectionType.EDGES)) {
			try {
				arango.db().createCollection(name, new CollectionCreateOptions().type(CollectionType.EDGES));
				log.info("'{}' edge-collection has created!", name);
			} catch (ArangoDBException e) {
				log.error(e.getErrorMessage());
			}
		}
		return arango.db().graph(graphName).edgeCollection(name);
	}

	public ArangoGraph createGraph(String graphName, Collection<EdgeDefinition> edgeDefinitions) {
		if (!aDb.graph(graphName).exists()) {
			try {
				arango.db().createGraph(graphName, edgeDefinitions, new GraphCreateOptions());
				log.info("'{}' edge-collection has created!", graphName);
			} catch (ArangoDBException e) {
				log.error(e.getErrorMessage());
			}
		}
		return arango.db().graph(graphName);
	}

	public void deleteDatabase(String name) {
		arango.db(name).drop();
	}

	public String getCollectionName() {
		return collectionName;
	}

	public String getEdgeCollectionName() {
		return edgeCollectionName;
	}

	public String getGraphName() {
		return graphName;
	}
}
