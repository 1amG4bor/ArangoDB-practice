package com.epam.arango_practice.service;

import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.model.RelationEdge;

import java.util.Collection;

/**
 * Interface for operations of node-relations
 */
public interface RelationService {
	RelationEdge getEdge(String key);

	Collection<RelationEdge> getAllEdges();

	Collection<Member> getSiblings(String name);

	Collection<Member> getGrandparents(String name);

	void addEdge(RelationEdge entity);

	void addAllEdges(Collection<RelationEdge> edges);
}