package com.epam.arango_practice.repository.stage;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDatabase;
import com.epam.arango_practice.configuration.ArangoConfig;
import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.repository.FamilyRepository;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.AfterStage;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.integration.spring.JGivenStage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@JGivenStage
public class FamilyRepositoryStage extends Stage<FamilyRepositoryStage> {
	private final String DB_NAME = "testDB";
	private final String COLLECTION_NAME = "testNodes";

	private ArangoDatabase arangoDB = null;
	private ArangoCollection familyCollection = null;
	private ArangoConfig arangoConfig = null;
	private FamilyRepository repository = null;
	private Collection<Member> expectedFamily = null;
	private Collection<Member> providedFamily = null;
	private Member requestedMember = null;

	@BeforeScenario
	public void init() {
		String EDGE_COLLECTION_NAME = "testEdges";
		arangoConfig = new ArangoConfig(DB_NAME, COLLECTION_NAME, EDGE_COLLECTION_NAME);
		arangoConfig.initIt();
		arangoDB = arangoConfig.createDatabase(DB_NAME);
		repository = new FamilyRepository(arangoConfig);
		familyCollection = arangoConfig.createCollection(COLLECTION_NAME);
	}

	@AfterScenario
	public void destroy() {
		arangoConfig.deleteDatabase(DB_NAME);
	}

	@AfterStage
	public void clearDB() {
		arangoDB.collection(COLLECTION_NAME).truncate();
	}

	public FamilyRepositoryStage a_family(Collection<Member> family) {
		expectedFamily = family;
		expectedFamily.forEach(familyCollection::insertDocument);
		return this;
	}

	public FamilyRepositoryStage you_ask_for_the_family() {
		providedFamily = repository.getFamily();
		return this;
	}

	public FamilyRepositoryStage you_get_all_of_the_members() {
		boolean check1 = expectedFamily.containsAll(providedFamily);
		boolean check2 = providedFamily.containsAll(expectedFamily);
		assertTrue("providedFamily should contains all members from expectedFamily", check1 && check2);
		return this;
	}

	public FamilyRepositoryStage you_ask_for_$_members(Gender gender) {
		providedFamily = repository.getHomogeneous(gender);
		return this;
	}

	public FamilyRepositoryStage you_get_$_member(int i) {
		assertEquals("collection size should be " + i, i, providedFamily.size());
		return this;
	}

	public FamilyRepositoryStage you_ask_for_the_family_sorted_by_birth() {
		providedFamily = repository.getFamilyByBirthdate();
		return this;
	}

	public FamilyRepositoryStage you_get_$(int index, Member member) {
		List<Member> familyArray = new ArrayList<>(providedFamily);
		assertEquals(index + ". item should be " + member.getName(), member, familyArray.get(index));
		return this;
	}

	public FamilyRepositoryStage you_ask_for_the_family_sorted_by_name() {
		providedFamily = repository.getFamilyByName();
		return this;
	}

	public FamilyRepositoryStage you_ask_for_the_member_$(String key) {
		requestedMember = repository.getMember(key);
		return this;
	}

	public FamilyRepositoryStage you_get_member_$(Member member) {
		assertEquals("The response should be the same", member, requestedMember);
		return this;
	}

	public FamilyRepositoryStage you_want_to_add_a_member(Member newMember) {
		repository.addMember(newMember);
		return this;
	}

	public FamilyRepositoryStage the_family_size_will_be_$(int size) {
		assertEquals("The family size should be: ", size, repository.getFamily().size());
		return this;
	}

	public FamilyRepositoryStage you_want_to_add_more_members(Collection<Member> members) {
		repository.addAllMember(members);
		return this;
	}

	public FamilyRepositoryStage you_want_to_modify_a_member(Member modifiedMember) {
		repository.modifyMember(modifiedMember);
		requestedMember = repository.getMember(modifiedMember.getKey());
		return this;
	}

	public FamilyRepositoryStage you_want_to_delete_a_member(Member member) {
		repository.deleteMember(member.getKey());
		return this;
	}
}


