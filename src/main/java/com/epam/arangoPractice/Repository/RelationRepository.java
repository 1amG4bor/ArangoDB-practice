package com.epam.arangoPractice.Repository;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.EdgeEntity;
import com.arangodb.util.MapBuilder;
import com.epam.arangoPractice.configuration.ArangoConfig;
import com.epam.arangoPractice.model.Member;
import com.epam.arangoPractice.model.RelationEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class RelationRepository {
    private final String GET_EDGE_BY_KEY = "FOR item IN @@COLLECTION FILTER item._key == @KEY RETURN item";
    private final String CREATE_EDGE = "INSERT { _from: @CHILD, _to: @PARENT} INTO @@COLLECTION";
    private final String GET_EDGES = "FOR item IN @@COLLECTION RETURN item";
    private final String GET_ALL_GRANDPARENTS_SORTED_BY_BIRTHDATE =
            "FOR member IN @@COLLECTION FILTER member.name == @NAME\n" +
                    "FOR v1, e1, p1 IN 1..1 OUTBOUND member GRAPH @GRAPH\n" +
                    "  LET parents = ( FILTER e1.relation != @FILTER RETURN v1)\n" +
                    "    FOR item IN parents\n" +
                    "      FOR v, e, p IN 1..1 OUTBOUND item GRAPH @GRAPH\n" +
                    "        FILTER e.relation != @FILTER SORT v.birthDate RETURN DISTINCT v";
    private final String GET_ALL_SIBLINGS_SORTED_BY_BIRTHDATE =
            "FOR member IN @@COLLECTION FILTER member.name == @NAME\n" +
                    "FOR v1, e1, p1 IN 1..1 OUTBOUND member GRAPH @GRAPH\n" +
                    "  LET parents = (FILTER e1.relation != @FILTER RETURN v1)\n" +
                    "    FOR item IN parents\n" +
                    "      FOR v, e, p IN 1..1 INBOUND item GRAPH @GRAPH\n" +
                    "        FILTER v.name != @NAME FILTER e.relation != @FILTER SORT v.birthDate RETURN DISTINCT v";

    private ArangoDatabase aDb;
    private MapBuilder mapBuilder = new MapBuilder();
    private ArangoConfig config;

    @Autowired
    public RelationRepository(ArangoConfig config) {
        this.config = config;
        this.aDb = config.getDb();
    }

    public EdgeEntity getEdge(String key, String edgeCollectionName) {
        ArangoCursor<EdgeEntity> cursor = aDb.query(GET_EDGE_BY_KEY,
                mapBuilder
                        .put("KEY", key)
                        .put("@COLLECTION", edgeCollectionName)
                        .get(), null, EdgeEntity.class);
        return cursor.next();
    }

    public EdgeDefinition addEdge(RelationEdge edge, String edgeCollectionName) {
        ArangoCursor<EdgeDefinition> cursor = aDb.query(CREATE_EDGE,
                mapBuilder
                        .put("CHILD", edge.getFrom())
                        .put("PARENT", edge.getTo())
                        .put("@COLLECTION", edgeCollectionName)
                        .get(), null, EdgeDefinition.class);
        return cursor.next();
    }

    public Collection<EdgeEntity> getAllEdges(String edgeCollectionName) {
        ArangoCursor<EdgeEntity> cursor = aDb.query(GET_EDGES,
                mapBuilder
                        .put("@COLLECTION", edgeCollectionName)
                        .get(), null, EdgeEntity.class);
        ArrayList<EdgeEntity> result = new ArrayList<>();
        cursor.forEach(result::add);
        return result;
    }

    public Collection<Member> getAllGrandparents_sorted(String name, String collectionName, String edgeCollectionName) {
        ArangoCursor<Member> cursor = aDb.query(GET_ALL_GRANDPARENTS_SORTED_BY_BIRTHDATE,
                mapBuilder
                        .put("NAME", name)
                        .put("@COLLECTION", collectionName)
                        .put("GRAPH", config.getGraphName())
                        .put("FILTER", "marriage" )
                        .get(), null, Member.class);
        ArrayList<Member> result = new ArrayList<>();
        cursor.forEach(result::add);
        return result;
    }

    public Collection<Member> getAllSiblings_sorted(String name, String collectionName, String edgeCollectionName) {
        ArangoCursor<Member> cursor = aDb.query(GET_ALL_SIBLINGS_SORTED_BY_BIRTHDATE,
                mapBuilder
                        .put("NAME", name)
                        .put("@COLLECTION", collectionName)
                        .put("GRAPH", config.getGraphName())
                        .put("FILTER", "marriage" )
                        .get(), null, Member.class);
        ArrayList<Member> result = new ArrayList<>();
        cursor.forEach(result::add);
        return result;
    }
}
