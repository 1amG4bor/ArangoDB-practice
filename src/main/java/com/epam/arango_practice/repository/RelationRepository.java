package com.epam.arango_practice.repository;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import com.epam.arango_practice.configuration.ArangoConfig;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.model.RelationEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

/**
 * Repository to read/modify the data of 'relations' (between the family-members)
 */
@Repository
public class RelationRepository {
	private String FAMILY_COLLECTION_NAME;
	private String RELATION_EDGECOLLECTION_NAME;
	private String GRAPH_NAME;
	private final String GET_EDGE_BY_KEY = "FOR item IN @@COLLECTION FILTER item._key == @KEY RETURN item";
	private final String CREATE_EDGE = "INSERT { _from: @CHILD, _to: @PARENT} INTO @@COLLECTION";
	private final String GET_EDGES = "FOR item IN @@COLLECTION RETURN item";
	private final String GET_ALL_GRANDPARENTS_SORTED_BY_BIRTHDATE =
			"FOR member IN @@COLLECTION FILTER member.name == @NAME\n"
					+ "FOR v1, e1, p1 IN 1..1 OUTBOUND member GRAPH @GRAPH\n"
					+ "  LET parents = ( FILTER e1.relation != @FILTER RETURN v1)\n"
					+ "    FOR item IN parents\n"
					+ "      FOR v, e, p IN 1..1 OUTBOUND item GRAPH @GRAPH\n"
					+ "        FILTER e.relation != @FILTER SORT v.birthDate RETURN DISTINCT v";
	private final String GET_ALL_SIBLINGS_SORTED_BY_BIRTHDATE =
			"FOR member IN @@COLLECTION FILTER member.name == @NAME\n"
					+ "FOR v1, e1, p1 IN 1..1 OUTBOUND member GRAPH @GRAPH\n"
					+ "  LET parents = (FILTER e1.relation != @FILTER RETURN v1)\n"
					+ "    FOR item IN parents\n"
					+ "      FOR v, e, p IN 1..1 INBOUND item GRAPH @GRAPH\n"
					+ "        FILTER v.name != @NAME FILTER e.relation != @FILTER SORT v.birthDate RETURN DISTINCT v";

	private ArangoDatabase aDb;
	private MapBuilder mapBuilder = new MapBuilder();
	private ArangoConfig config;

	@Autowired
	public RelationRepository(ArangoConfig config) {
		this.config = config;
		this.aDb = config.getaDb();
		FAMILY_COLLECTION_NAME = config.getCollectionName();
		RELATION_EDGECOLLECTION_NAME = config.getEdgeCollectionName();
		GRAPH_NAME = config.getGraphName();
	}

	public RelationEdge getEdge(String key) {
		ArangoCursor<RelationEdge> cursor = aDb.query(GET_EDGE_BY_KEY,
				mapBuilder
						.put("KEY", key)
						.put("@COLLECTION", RELATION_EDGECOLLECTION_NAME)
						.get(), null, RelationEdge.class);
		return cursor.next();
	}

	public Collection<RelationEdge> getAllEdges() {
		ArangoCursor<RelationEdge> cursor = aDb.query(
				GET_EDGES, mapBuilder.put("@COLLECTION", RELATION_EDGECOLLECTION_NAME).get(), null, RelationEdge.class);
		Collection<RelationEdge> result = cursor.asListRemaining();
		return result;
	}

	// Get all siblings sorted by birth-date
	public Collection<Member> getSiblings(String name) {
		ArangoCursor<Member> cursor = runCompactQuery(GET_ALL_SIBLINGS_SORTED_BY_BIRTHDATE, name);
		return cursor.asListRemaining();
	}

	// Get all grandparents sorted by birth-date
	public Collection<Member> getGrandparents(String name) {
		ArangoCursor<Member> cursor = runCompactQuery(GET_ALL_GRANDPARENTS_SORTED_BY_BIRTHDATE, name);
		return cursor.asListRemaining();
	}

	private ArangoCursor<Member> runCompactQuery(String QUERY, String startVectorName) {
		return aDb.query(QUERY, buildMap(startVectorName), null, Member.class);
	}

	private Map<String, Object> buildMap(String name) {
		return mapBuilder
				.put("NAME", name)
				.put("@COLLECTION", FAMILY_COLLECTION_NAME)
				.put("GRAPH", GRAPH_NAME)
				.put("FILTER", "marriage")
				.get();
	}

	public void addEdge(RelationEdge edge) {
		aDb.graph(GRAPH_NAME).edgeCollection(RELATION_EDGECOLLECTION_NAME).insertEdge(edge);
	}

	public void addAllEdges(Collection<RelationEdge> edges) {
		edges.stream().forEach(this::addEdge);
	}
}
