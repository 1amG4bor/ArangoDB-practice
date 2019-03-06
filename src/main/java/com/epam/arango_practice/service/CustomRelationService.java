package com.epam.arango_practice.service;

import com.epam.arango_practice.repository.RelationRepository;
import com.epam.arango_practice.configuration.ArangoConfig;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.model.RelationEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service for relation operations
 */
@Service
public class CustomRelationService implements RelationService {
	private ArangoConfig config;
	private RelationRepository relationRepository;

	@Autowired
	public CustomRelationService(ArangoConfig config, RelationRepository relationRepository) {
		this.config = config;
		this.relationRepository = relationRepository;
	}

	@Override
	public RelationEdge getEdge(String key) {
		return relationRepository.getEdge(key);
	}

	@Override
	public Collection<RelationEdge> getAllEdges() {
		return relationRepository.getAllEdges();
	}

	@Override
	public Collection<Member> getSiblings(String name) {
		return relationRepository.getSiblings(name);
	}

	@Override
	public Collection<Member> getGrandparents(String name) {
		return relationRepository.getGrandparents(name);
	}

	@Override
	public void addEdge(RelationEdge edge) {
		relationRepository.addEdge(edge);
	}

	@Override
	public void addAllEdges(Collection<RelationEdge> edges) {
		relationRepository.addAllEdges(edges);
	}
}
