package com.epam.arangoPractice.controller;

import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.EdgeEntity;
import com.epam.arangoPractice.model.RelationEdge;
import com.epam.arangoPractice.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/relation/")
public class RelationController {
    private RelationService edgeService;

    @Autowired
    public RelationController(RelationService edgeService) {
        this.edgeService = edgeService;
    }

    @GetMapping("/edge/{key}")
    public ResponseEntity<EdgeEntity> getEdge(@PathVariable String key) {
        return new ResponseEntity<>(edgeService.getEdge(key), HttpStatus.OK);
    }

    @GetMapping("/edges/all")
    public ResponseEntity<Collection<EdgeEntity>> getAllEdges() {
        return new ResponseEntity<>(edgeService.getAllEdges(), HttpStatus.OK);
    }

    @PostMapping("/edge/add")
    @ResponseStatus(HttpStatus.OK)
    public EdgeEntity addEdge(@RequestBody RelationEdge edge) {
        return edgeService.addEdge(edge);
    }

    @PostMapping("/edges/add")
    @ResponseStatus(HttpStatus.OK)
    public Collection<EdgeEntity> addAllEdges(@RequestBody String jsonString) {
        return edgeService.addAllEdges(jsonString);
    }
}
