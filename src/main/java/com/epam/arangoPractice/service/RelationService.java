package com.epam.arangoPractice.service;

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoEdgeCollection;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.EdgeEntity;
import com.arangodb.model.EdgeCreateOptions;
import com.epam.arangoPractice.Repository.RelationRepository;
import com.epam.arangoPractice.configuration.ArangoConfig;
import com.epam.arangoPractice.model.Member;
import com.epam.arangoPractice.model.RelationEdge;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelationService {
    @Value("${arangodb.edgeCollections}")
    private  String edgeCollectionName;
    @Value("${arangodb.nodecollections}")
    private  String collectionName;

    private ArangoConfig config;
    private RelationRepository relationRepository;
    private ArangoDatabase aDB;
    private ArangoEdgeCollection edgeCollection;
    private ArangoGraph graph;

    @Autowired
    public RelationService(ArangoConfig config, RelationRepository relationRepository) {
        this.config = config;
        this.relationRepository = relationRepository;

        this.aDB = config.getDb();
        edgeCollection = config.getEdgeCollection(edgeCollectionName);
        graph = config.getGraph("test", edgeCollection);
    }

    public EdgeEntity getEdge(String key) {
        EdgeEntity edge = relationRepository.getEdge(key, edgeCollectionName);
        return edge;
    }

    public Collection<EdgeEntity> getAllEdges() {
        return relationRepository.getAllEdges(edgeCollectionName);
    }

    public EdgeEntity addEdge(RelationEdge entity) {
        return edgeCollection.insertEdge(entity, new EdgeCreateOptions());
    }

    public Collection<EdgeEntity> addAllEdges(String jsonString) {
        jsonString = jsonString.replaceAll("\\s+","");
        Gson gson = new Gson();
        List<RelationEdge> edgeList = Arrays.stream(gson.fromJson(jsonString, RelationEdge[].class)).collect(Collectors.toList());
        return edgeList.stream().map(this::addEdge).collect(Collectors.toList());
    }

    public EdgeDefinition addEdgeWithAQL(RelationEdge entity) {
        return relationRepository.addEdge(entity, edgeCollectionName);
    }

    public Collection<Member> getCousins(String name) {
        return relationRepository.getAllSiblings_sorted(name, collectionName, edgeCollectionName);
    }

    public Collection<Member> getAllGrandParentsSortedByBirthDate(String name) {
        return relationRepository.getAllGrandparents_sorted(name, collectionName, edgeCollectionName);
    }
}
