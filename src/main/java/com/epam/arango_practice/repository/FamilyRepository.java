package com.epam.arango_practice.repository;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import com.epam.arango_practice.configuration.ArangoConfig;
import com.epam.arango_practice.model.Gender;
import com.epam.arango_practice.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repository to read/modify data of 'family-members' or the 'family' (Collection<Member>)
 */
@Repository
@Slf4j
public class FamilyRepository {
	private String FAMILY_COLLECTION_NAME;
	private final String GET_FAMILY = "FOR member IN @@COLLECTION RETURN member";
	private final String GET_HOMOGENEOUS_FAMILY_MEMBERS = "FOR member IN @@COLLECTION FILTER member.gender == @gender RETURN member";
	private final String GET_FAMILY_SORTED_BY_NAME = "FOR member IN @@COLLECTION SORT member.name RETURN member";
	private final String GET_FAMILY_SORTED_BY_BIRTH = "FOR member IN @@COLLECTION SORT member.birthDate RETURN member";
	private final String GET_MEMBER = "FOR member IN @@COLLECTION FILTER member.name == @name RETURN member";
	private ArangoConfig arangoConfig;
	private ArangoDatabase aDb;
	private MapBuilder mapBuilder = new MapBuilder();

	@Autowired
	public FamilyRepository(ArangoConfig arangoConfig) {
		this.arangoConfig = arangoConfig;
		this.aDb = arangoConfig.getaDb();
		FAMILY_COLLECTION_NAME = arangoConfig.getCollectionName();
	}

	public Collection<Member> getFamily() {
		ArangoCursor<Member> cursor = aDb.query(GET_FAMILY, mapBuilder.put("@COLLECTION", FAMILY_COLLECTION_NAME).get(), null, Member.class);
		return cursor.asListRemaining();
	}

	public Collection<Member> getHomogeneous(Gender gender) {
		ArangoCursor<Member> cursor = aDb.query(GET_HOMOGENEOUS_FAMILY_MEMBERS, mapBuilder.put("@COLLECTION", FAMILY_COLLECTION_NAME).put("gender", gender.toString()).get(), null, Member.class);
		return cursor.asListRemaining();
	}

	public Collection<Member> getFamilyByBirthdate() {
		ArangoCursor<Member> cursor = aDb.query(GET_FAMILY_SORTED_BY_BIRTH, mapBuilder.put("@COLLECTION", FAMILY_COLLECTION_NAME).get(), null, Member.class);
		return cursor.asListRemaining();
	}

	public Collection<Member> getFamilyByName() {
		ArangoCursor<Member> cursor = aDb.query(GET_FAMILY_SORTED_BY_NAME, mapBuilder.put("@COLLECTION", FAMILY_COLLECTION_NAME).get(), null, Member.class);
		return cursor.asListRemaining();
	}

	public Member getMember(String key) {
		Member result = aDb.collection(FAMILY_COLLECTION_NAME).getDocument(key, Member.class);
		return result;
	}

	public Member getMemberByName(String name) {
		ArangoCursor<Member> cursor = aDb.query(GET_MEMBER, mapBuilder.put("@name", name).put("@COLLECTION", FAMILY_COLLECTION_NAME).get(),
				null, Member.class);
		return cursor.next();
	}

	public void addMember(Member member) {
		try {
			aDb.collection(FAMILY_COLLECTION_NAME).insertDocument(member);
		} catch (ArangoDBException e) {
			log.error("Failed to add member named '{}'. Error: {}", member.getName(), e.getErrorMessage());
			throw new RuntimeException("Failed to add member: " + e.getLocalizedMessage());
		}
	}

	public void addAllMember(Collection<Member> members) {
		try {
			aDb.collection(FAMILY_COLLECTION_NAME).insertDocuments(members);
		} catch (ArangoDBException e) {
			log.error("Failed to add members. Error: {}", e.getErrorMessage());
			throw new RuntimeException("Failed to add member-list: " + e.getLocalizedMessage());
		}
	}

	public void modifyMember(Member member) {
		try {
			aDb.collection(FAMILY_COLLECTION_NAME).updateDocument(member.getKey(), member);
		} catch (ArangoDBException e) {
			log.error("Failed to modify member with key: '{}'. Error: {}", member.getKey(), e.getErrorMessage());
			throw new RuntimeException("Failed to modify member: " + e.getLocalizedMessage());
		}
	}

	public void deleteMember(String key) {
		try {
			aDb.collection(FAMILY_COLLECTION_NAME).deleteDocument(key);
		} catch (ArangoDBException e) {
			log.error("Failed to delete member with key: '{}'. Error: {}", key, e.getErrorMessage());
			throw new RuntimeException("Failed to delete member: " + e.getLocalizedMessage());
		}
	}
}
