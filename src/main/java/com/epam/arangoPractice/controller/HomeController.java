package com.epam.arangoPractice.controller;

import com.epam.arangoPractice.model.Member;
import com.epam.arangoPractice.service.FamilyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    private FamilyService familyService;

    public HomeController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @GetMapping("/family")
    public ResponseEntity<Set<Member>> getFamily() {
        return new ResponseEntity<Set<Member>>(familyService.getFamily(), HttpStatus.OK);
    }

    @GetMapping("/family/sort")
    public ResponseEntity<Set<Member>> getFamilyByBirthdate(@RequestParam String by) {
        if (by.equals("birth") || by.equals("name")) {
            return by.equals("name") ?
                new ResponseEntity<Set<Member>>(familyService.getFamilyByName(), HttpStatus.OK) :
                new ResponseEntity<Set<Member>>(familyService.getFamilyByBirthdate(), HttpStatus.OK);
        }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/member/")
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        System.out.println(member);
        return new ResponseEntity<>(familyService.addMember(member), HttpStatus.OK);
    }

    @PutMapping("/member/save")
    public ResponseEntity<Member> saveMember(@RequestBody Member member) {
        return new ResponseEntity<>(familyService.saveMember(member), HttpStatus.OK);
    }

    @DeleteMapping("/member/{id}/delete")
    public ResponseEntity<Object> deleteMember(@PathVariable Long id) {
        if (familyService.deleteMember(id)) {
            return new ResponseEntity<>("{\"deleted id\": " + id + "}", HttpStatus.OK);
        }
        return new ResponseEntity<>("Error has happened!", HttpStatus.BAD_REQUEST);
    }
}
