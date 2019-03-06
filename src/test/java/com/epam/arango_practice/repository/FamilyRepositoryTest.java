package com.epam.arango_practice.repository;

import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.repository.stage.FamilyRepositoryStage;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collection;

public class FamilyRepositoryTest extends SimpleScenarioTest<FamilyRepositoryStage> {
	private final Member dad = new Member("dad", "1950-01-01", Gender.MALE);
	private final Member mom = new Member("mom", "1955-01-01", Gender.FEMALE);
	private final Member me = new Member("me", "1980-01-01", Gender.MALE);
	private Collection<Member> family = Arrays.asList(dad, mom, me);

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void getFamily_TEST() {
		given().a_family(family);
		when().you_ask_for_the_family();
		then().you_get_all_of_the_members();

	}

	@Test
	public void getHomogeneousFemale_TEST() {
		given().a_family(family);
		when().you_ask_for_$_members(Gender.FEMALE);
		then().you_get_$_member(1);
	}

	@Test
	public void getHomogeneousMale_TEST() {
		given().a_family(family);
		when().you_ask_for_$_members(Gender.MALE);
		then().you_get_$_member(2);
	}

	@Test
	public void getFamilyByBirthdate_TEST() {
		given().a_family(Arrays.asList(me, dad, mom));
		when().you_ask_for_the_family_sorted_by_birth();
		then().you_get_$(0, dad)
				.and().you_get_$(1, mom)
				.and().you_get_$(2, me);
	}

	@Test
	public void getFamilyByName_TEST() {
		given().a_family(Arrays.asList(me, dad, mom));
		when().you_ask_for_the_family_sorted_by_name();
		then().you_get_$(0, dad)
				.and().you_get_$(1, me)
				.and().you_get_$(2, mom);
	}

	@Test
	public void getMember_TEST() {
		given().a_family(family);
		when().you_ask_for_the_member_$(mom.getKey());
		then().you_get_member_$(mom);
	}

	@Test
	public void getMember_withInvalidKey_TEST() {
		given().a_family(family);
		when().you_ask_for_the_member_$("0123456789");
		then().you_get_member_$(null);
	}

	@Test
	public void addMember_TEST() {
		given().a_family(family);
		Member newMember = new Member("my dog", "2014-01-01", Gender.FEMALE);
		when().you_want_to_add_a_member(newMember);
		then().the_family_size_will_be_$(4);
	}

	@Test
	public void addAllMember_TEST() {
		Collection<Member> newMembers = Arrays.asList(mom, dad);
		given().a_family(Arrays.asList(me));
		when().you_want_to_add_more_members(newMembers);
		then().the_family_size_will_be_$(3);
	}

	@Test
	public void modifyMember_TEST() {
		given().a_family(family);
		me.setName("It's me!");
		when().you_want_to_modify_a_member(me);
		then().you_get_member_$(me);
	}

	@Test
	public void modifyMember_withoutKey_TEST() {
		given().a_family(family);
		Member newMe = new Member("It's me!", "2000-01-01", Gender.MALE);
		exceptionRule.expect(RuntimeException.class);
		when().you_want_to_modify_a_member(newMe);
	}

	@Test
	public void deleteMember_TEST() {
		given().a_family(family);
		when().you_want_to_delete_a_member(me);
		then().the_family_size_will_be_$(2);
	}

	@Test
	public void deleteMember_withoutKey_TEST() {
		given().a_family(family);
		Member unknown = new Member("unknown", "0001-01-01", Gender.MALE);
		exceptionRule.expect(RuntimeException.class);
		when().you_want_to_delete_a_member(unknown);
	}
}