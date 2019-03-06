package com.epam.arango_practice.service;

import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;
import com.epam.arango_practice.repository.FamilyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service for family-document operations
 */
@Service
@Slf4j
public class CustomFamilyService implements FamilyService {
	private FamilyRepository repository;

	@Autowired
	public CustomFamilyService(FamilyRepository repository) {
		this.repository = repository;
	}

	@Override
	public Collection<Member> getFamily() {
		return repository.getFamily();
	}

	@Override
	public Collection<Member> getHomogeneous(Gender gender) {
		return repository.getHomogeneous(gender);
	}

	@Override
	public Collection<Member> getSortedFamily(SortBy sortBy) {
		switch (sortBy) {
			case BY_NAME:
				return repository.getFamilyByName();
			case BY_BIRTH:
			default:
				return repository.getFamilyByBirthdate();
		}
	}

	@Override
	public Member getMember(String key) {
		return repository.getMember(key);
	}

	@Override
	public void addMember(Member member) {
		repository.addMember(member);
	}

	@Override
	public void addAllMember(Collection<Member> members) {
		repository.addAllMember(members);
	}

	@Override
	public void modifyMember(Member member) {
		repository.modifyMember(member);
	}

	@Override
	public void deleteMember(String key) {
		repository.deleteMember(key);
	}
}
