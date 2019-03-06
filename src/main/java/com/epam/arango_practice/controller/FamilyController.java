package com.epam.arango_practice.controller;

import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.service.CustomFamilyService;
import com.epam.arango_practice.service.SortBy;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controller for provide 'Member' and 'Collection of Member' items, as well as add, modify and delete them
 */
@RestController
@RequestMapping("/api/")
@Api(value = "FamilyController", description = "Interface for query and modify the database.", position = 1, consumes = "application/json")
@Slf4j
public class FamilyController {
	private CustomFamilyService customFamilyService;
	private final String SORT_BY_VALUES = "NAME,BIRTH";
	private final String GENDER_VALUES = "MALE,FEMALE";

	@Autowired
	public FamilyController(CustomFamilyService customFamilyService) {
		this.customFamilyService = customFamilyService;
	}

	@ApiOperation(value = "Return a collection of members.", response = Member.class, responseContainer = "Collection")
	@GetMapping("/family")
	public ResponseEntity<Collection<Member>> getFamily() {
		return new ResponseEntity<>(customFamilyService.getFamily(), HttpStatus.OK);
	}

	@GetMapping("/family/sort")
	@ApiOperation(value = "Return a collection of members sorted by 'name' or 'birthdate'(age).",
					response = Member.class, responseContainer = "Collection")
	public ResponseEntity<Collection<Member>> getSortedFamily(
					@ApiParam(allowableValues = SORT_BY_VALUES, example = "NAME", required = true) @RequestParam String by) {
		SortBy sortBy = SortBy.valueOf("BY_" + by.toUpperCase());
		return new ResponseEntity<>(customFamilyService.getSortedFamily(sortBy), HttpStatus.OK);
	}

	@GetMapping("/member/{key}")
	@ApiOperation(value = "Return a member.", response = Member.class)
	public ResponseEntity<Member> getMember(@ApiParam(required = true, example = "672943") @PathVariable String key) {
		return new ResponseEntity<>(customFamilyService.getMember(key), HttpStatus.OK);
	}

	@PostMapping("/member")
	@ApiOperation(value = "Insert a member into database.", response = String.class, consumes = "application/json")
	public ResponseEntity<String> addMember(@ApiParam(required = true) @RequestBody Member member) {
		customFamilyService.addMember(member);
		return new ResponseEntity<>("{\"message\" : \"Member was added succesfully!\"}", HttpStatus.OK);
	}

	@PostMapping("/members")
	@ApiOperation(value = "Insert more members into database.", response = String.class, consumes = "application/json")
	public ResponseEntity<String> addAll(@ApiParam(required = true) @RequestBody Collection<Member> members) {
		customFamilyService.addAllMember(members);
		return new ResponseEntity<>("{\"message\" : \"All member was added succesfully!\"}", HttpStatus.OK);
	}

	@PutMapping("/member/save")
	@ApiOperation(value = "Modify an existing member in the database.", response = String.class, consumes = "application/json")
	public ResponseEntity<String> modifyMember(@ApiParam(required = true) @RequestBody Member member) {
		customFamilyService.modifyMember(member);
		return new ResponseEntity<>("Member saved succusfully!", HttpStatus.OK);
	}

	@DeleteMapping("/member/{key}")
	@ApiOperation(value = "Delete an existing member in the database.", response = String.class, consumes = "application/json")
	public ResponseEntity<String> deleteMember(@ApiParam(required = true, example = "672943") @PathVariable String key) {
		customFamilyService.deleteMember(key);
		return new ResponseEntity<>("{\"Member has deleted with key\": " + key + "}", HttpStatus.OK);
	}

	@GetMapping("/members/filter")
	@ApiOperation(value = "Return homogeneous members from the database.", response = String.class, responseContainer = "list")
	public ResponseEntity<Collection<Member>> getHomogeneousMembers(
					@ApiParam(required = true, allowableValues = GENDER_VALUES, example = "672943", value = "MALE")
					@RequestParam Gender gender) {
		return new ResponseEntity<>(customFamilyService.getHomogeneous(gender), HttpStatus.OK);
	}
}
