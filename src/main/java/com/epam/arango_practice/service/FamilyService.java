package com.epam.arango_practice.service;

import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;

import java.util.Collection;

/**
 * Interface for operations of Member
 */
public interface FamilyService {
	Collection<Member> getFamily();

	Collection<Member> getHomogeneous(Gender gender);

	Collection<Member> getSortedFamily(SortBy sortBy);

	Member getMember(String key);

	void addMember(Member member);

	void addAllMember(Collection<Member> member);

	void modifyMember(Member member);

	void deleteMember(String key);
}
