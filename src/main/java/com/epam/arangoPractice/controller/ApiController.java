package com.epam.arangoPractice.controller;

import com.epam.arangoPractice.model.Member;
import com.epam.arangoPractice.service.FamilyService;
import com.epam.arangoPractice.service.SortBy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/")
@Slf4j
public class ApiController {
    private FamilyService familyService;

    @Autowired
    public ApiController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @GetMapping("/family")
    public ResponseEntity<List<Member>> getFamily() {
        return new ResponseEntity<>(familyService.getFamily(), HttpStatus.OK);
    }

    @GetMapping("/family/sort")
    public ResponseEntity<List<Member>> getSortedFamily(@RequestParam String by) {
        SortBy sortBy = SortBy.valueOf("BY_" + by.toUpperCase());
        return new ResponseEntity<>(familyService.getSortedFamily(sortBy), HttpStatus.OK);
    }

    @PostMapping("/member")
    public ResponseEntity<String> addMember(@RequestBody Member member) {
        log.debug(member.toString());
        familyService.addMember(member);
        return new ResponseEntity<>("{\"message\" : \"Member was added succesfully!\"}", HttpStatus.OK);
    }

    @PostMapping("/members")
    public ResponseEntity<String> addAll(@RequestBody String jsonString) {
        familyService.addAll(jsonString);
        return new ResponseEntity<>("{\"message\" : \"All member was added succesfully!\"}", HttpStatus.OK);
    }

    @PutMapping("/member/save")
    public ResponseEntity<String> modifyMember(@RequestBody Member member) {
        familyService.modifyMember(member);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @DeleteMapping("/member/{key}")
    public ResponseEntity<String> deleteMember(@PathVariable String key) {
        familyService.deleteMember(key);
        return new ResponseEntity<>("{\"deleted id\": " + key + "}", HttpStatus.OK);
    }
}
