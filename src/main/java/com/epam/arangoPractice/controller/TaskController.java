package com.epam.arangoPractice.controller;

import com.epam.arangoPractice.model.Gender;
import com.epam.arangoPractice.model.Member;
import com.epam.arangoPractice.service.FamilyService;
import com.epam.arangoPractice.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/task/")
public class TaskController {
    private RelationService relationService;
    private FamilyService familyService;

    @Autowired
    public TaskController(RelationService relationService, FamilyService familyService) {
        this.relationService = relationService;
        this.familyService = familyService;
    }

    // todo
    @GetMapping("/siblings/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Member> getCousins(@PathVariable String name) {
        return relationService.getCousins(name);
    }

    // todo
    @GetMapping("/grandparents/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Member> getAllGrandParentsByBirthDate(@PathVariable String name) {
        return relationService.getAllGrandParentsSortedByBirthDate(name);
    }

    @GetMapping("/members/filter")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Member> getSameGender(@RequestParam Gender gender) {
        return familyService.getFamily(gender);
    }


}
