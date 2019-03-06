package com.epam.arango_practice.service.stage;

import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.repository.FamilyRepository;
import com.epam.arango_practice.service.CustomFamilyService;
import com.epam.arango_practice.service.FamilyService;
import com.epam.arango_practice.service.SortBy;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterStage;
import com.tngtech.jgiven.annotation.BeforeScenario;
import org.mockito.Mockito;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

public class FamilyServiceStage extends Stage<FamilyServiceStage> {
	private Member dad = new Member("dad", "1950-01-01", Gender.MALE);
	private Member mom = new Member("mom", "1955-01-01", Gender.FEMALE);
	private Member me = new Member("me", "1980-01-01", Gender.MALE);
	private List<Member> family;
	private FamilyRepository repositoryMock = null;
	private FamilyService familyService = null;

	private Collection<Member> expectedFamily = null;
	private Collection<Member> providedFamily = null;
	private Member requestedMember = null;

	public FamilyServiceStage() {
		family = new ArrayList<>(Arrays.asList(dad, mom, me));
	}

	@BeforeScenario
	public void init() {
		repositoryMock = mock(FamilyRepository.class);
		Mockito.when(repositoryMock.getFamily()).thenReturn(family);
		Mockito.when(repositoryMock.getMember("001")).thenReturn(dad);
		Mockito.when(repositoryMock.getMember("002")).thenReturn(mom);
		Mockito.when(repositoryMock.getMember("003")).thenReturn(me);
		Mockito.when(repositoryMock.getMemberByName("dad")).thenReturn(dad);
		Mockito.when(repositoryMock.getMemberByName("mom")).thenReturn(mom);
		Mockito.when(repositoryMock.getMemberByName("me")).thenReturn(me);
		Mockito.when(repositoryMock.getHomogeneous(Gender.MALE)).thenReturn(Arrays.asList(dad, me));
		Mockito.when(repositoryMock.getHomogeneous(Gender.FEMALE)).thenReturn(Arrays.asList(mom));
		Mockito.when(repositoryMock.getFamilyByName()).thenReturn(Arrays.asList(dad, me, mom));
		Mockito.when(repositoryMock.getFamilyByBirthdate()).thenReturn(Arrays.asList(dad, mom, me));

		Mockito.doAnswer(invocation -> {
			Member member = invocation.getArgument(0);
			family.add(member);
			return null;
		}).when(repositoryMock).addMember(any(Member.class));

		Mockito.doAnswer(invocation -> {
			List<Member> members = invocation.getArgument(0);
			family.addAll(members);
			return null;
		}).when(repositoryMock).addAllMember(anyList());

		Mockito.doAnswer(invocation -> {
			Member newMe = invocation.getArgument(0);
			me.setName(newMe.getName());
			me.setGender(newMe.getGender());
			me.setBirthDate(newMe.getBirthDate());
			return null;
		}).when(repositoryMock).modifyMember(any(Member.class));

		Mockito.doAnswer(invocation -> {
			String key = invocation.getArgument(0);
			family.remove(familyService.getMember(key));
			return null;
		}).when(repositoryMock).deleteMember(anyString());

		familyService = new CustomFamilyService(repositoryMock);
	}

	private Member argThat(Member memberClass) {

		return null;
	}

	@AfterStage
	public void clearContainers() {
		expectedFamily = null;
		providedFamily = null;
		requestedMember = null;
	}

	public FamilyServiceStage you_ask_for_the_family() {
		providedFamily = familyService.getFamily();
		return self();
	}

	public FamilyServiceStage you_get_all_of_the_members(Collection<Member> family) {
		assertEquals(family, providedFamily);
		return self();
	}

	public FamilyServiceStage you_ask_for_homogeneous_members(Gender gender) {
		providedFamily = familyService.getHomogeneous(gender);
		return self();
	}

	public FamilyServiceStage you_get_$_member(int size) {
		assertEquals(size, providedFamily.size());
		return self();
	}


	public FamilyServiceStage you_ask_for_the_family_sorted_by_$(SortBy by) {
		providedFamily = familyService.getSortedFamily(by);
		return self();
	}

	public FamilyServiceStage you_get_$(int i, Member member) {
		List<Member> familyArray = new ArrayList<>(providedFamily);
		assertEquals(member, familyArray.get(i));
		return self();
	}

	public FamilyServiceStage you_ask_for_a_member_by_key(@NotNull String key) {
		requestedMember = familyService.getMember(key);
		return self();
	}

	public FamilyServiceStage you_get_back_the_member(Member member) {
		assertEquals(member, requestedMember);
		return self();
	}

	public FamilyServiceStage you_add_member(Member member) {
		familyService.addMember(member);
		return self();
	}

	public FamilyServiceStage the_family_size_will_be_$(int size) {
		providedFamily = familyService.getFamily();
		assertEquals(size, providedFamily.size());
		return self();
	}

	public FamilyServiceStage you_add_more_members(Collection<Member> members) {
		familyService.addAllMember(members);
		return self();
	}

	public FamilyServiceStage you_want_to_delete_a_member(Member member) {
		String key = getMockedKey(member);
		familyService.deleteMember(key);
		return self();
	}

	/**
	 * helping stub-method to get non-existent key what mocked
	 */
	private String getMockedKey(Member member) {
		switch (member.getName()) {
			case "dad":
				return "001";
			case "mom":
				return "002";
			case "me":
			case "It's me!":
				return "003";
		}
		return null;
	}

	public FamilyServiceStage you_want_to_modify_a_member(Member member) {
		familyService.modifyMember(member);
		return self();
	}

	public FamilyServiceStage you_get_member_$(Member member) {
		String key = getMockedKey(member);
		requestedMember = familyService.getMember(key);
		assertEquals(member, requestedMember);
		return self();
	}
}
