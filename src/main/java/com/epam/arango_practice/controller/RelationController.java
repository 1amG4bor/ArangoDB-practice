package com.epam.arango_practice.controller;

import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.model.RelationEdge;
import com.epam.arango_practice.service.CustomRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controller for perform special queries on the graph-database or expand it with adding more connects
 */
@RestController
@RequestMapping("api/relation/")
@Api(value = "RelationController", description = "Interface for perform special queries on the graph-database or expand it with adding more connects.",
				position = 2, consumes = "application/json")
public class RelationController {
	private CustomRelationService customRelationService;

	@Autowired
	public RelationController(CustomRelationService edgeService) {
		this.customRelationService = edgeService;
	}

	@GetMapping("/{name}/siblings")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Return the siblings of the given family-member.", response = Member.class, responseContainer = "Collection")
	public Collection<Member> getCousins(@ApiParam(required = true) @PathVariable String name) {
		return customRelationService.getSiblings(name);
	}

	// todo
	@GetMapping("/{name}/grandparents")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Return the grandparents of the given family-member.", response = Member.class, responseContainer = "Collection")
	public Collection<Member> getAllGrandParentsByBirthDate(@ApiParam(required = true) @PathVariable String name) {
		return customRelationService.getGrandparents(name);
	}

	@GetMapping("/edge/{key}")
	@ApiOperation(value = "Return a relation-edge .", response = RelationEdge.class)
	public ResponseEntity<RelationEdge> getEdge(@ApiParam(required = true) @PathVariable String key) {
		return new ResponseEntity<>(customRelationService.getEdge(key), HttpStatus.OK);
	}

	@GetMapping("/edges/all")
	@ApiOperation(value = "Return all of the relation-edges.", response = RelationEdge.class, responseContainer = "Collection")
	public ResponseEntity<Collection<RelationEdge>> getAllEdges() {
		return new ResponseEntity<>(customRelationService.getAllEdges(), HttpStatus.OK);
	}

	@PostMapping("/edge/add")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Insert a relation-edge into the database.", response = String.class)
	public ResponseEntity<String> addEdge(@ApiParam(required = true) @RequestBody RelationEdge edge) {
		customRelationService.addEdge(edge);
		return new ResponseEntity<>("Request completed, edge has been added to database.", HttpStatus.OK);
	}

	@PostMapping("/edges/add")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Insert more relation-edges into the database.", response = String.class)
	public ResponseEntity<String> addAllEdges(@ApiParam(required = true) @RequestBody Collection<RelationEdge> edgeCollection) {
		customRelationService.addAllEdges(edgeCollection);
		return new ResponseEntity<>("Request completed, edges have been added to database.", HttpStatus.OK);
	}
}
