package com.epam.arango_practice.service;

import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.service.stage.FamilyServiceStage;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class CustomFamilyServiceTest extends SimpleScenarioTest<FamilyServiceStage> {

	private Member dad = new Member("dad", "1950-01-01", Gender.MALE);
	private Member mom = new Member("mom", "1955-01-01", Gender.FEMALE);
	private Member me = new Member("me", "1980-01-01", Gender.MALE);

	@Test
	public void getFamily_TEST() {
		when().you_ask_for_the_family();
		then().you_get_all_of_the_members(Arrays.asList(dad, mom, me));
	}

	@Test
	public void getHomogeneousFemale_TEST() {
		when().you_ask_for_homogeneous_members(Gender.FEMALE);
		then().you_get_$_member(1);
	}

	@Test
	public void getHomogeneousMale_TEST() {
		when().you_ask_for_homogeneous_members(Gender.MALE);
		then().you_get_$_member(2);
	}


	@Test
	public void getSortedFamilyByName_TEST() {
		when().you_ask_for_the_family_sorted_by_$(SortBy.BY_NAME);
		then().you_get_$(0, dad)
				.and().you_get_$(1, me)
				.and().you_get_$(2, mom);
	}

	@Test
	public void getSortedFamilyByBirth_TEST() {
		when().you_ask_for_the_family_sorted_by_$(SortBy.BY_BIRTH);
		then().you_get_$(0, dad)
				.and().you_get_$(1, mom)
				.and().you_get_$(2, me);
	}

	@Test
	public void getMemberDad_TEST() {
		when().you_ask_for_the_family();
		when().you_ask_for_a_member_by_key("001");
		then().you_get_back_the_member(dad);
	}

	@Test
	public void getMemberMom_TEST() {
		when().you_ask_for_the_family();
		when().you_ask_for_a_member_by_key("002");
		then().you_get_back_the_member(mom);
	}

	@Test
	public void addMemberDog_TEST() {
		when().you_add_member(new Member("mr. Vau", "2014-01-01", Gender.FEMALE));
		then().the_family_size_will_be_$(4);
	}

	@Test
	public void addMemberCat_TEST() {
		when().you_add_member(new Member("miauu", "2000-01-01", Gender.FEMALE));
		then().the_family_size_will_be_$(4);
	}

	@Test
	public void addAllMember_TEST() {
		Collection<Member> moreMembers = Arrays.asList(
				new Member("mr. Vau", "2014-01-01", Gender.FEMALE),
				new Member("miauu", "2000-01-01", Gender.FEMALE)
		);
		when().you_add_more_members(moreMembers);
		then().the_family_size_will_be_$(5);
	}

	@Test
	public void modifyMember_Me_TEST() {
		me.setName("It's me!");
		when().you_want_to_modify_a_member(me);
		then().you_get_member_$(me);
	}

	@Test
	public void deleteMember_TEST() {
		when().you_want_to_delete_a_member(me);
		then().the_family_size_will_be_$(2);
	}
}