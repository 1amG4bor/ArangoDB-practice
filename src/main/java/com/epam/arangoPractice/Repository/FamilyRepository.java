package com.epam.arangoPractice.Repository;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import com.epam.arangoPractice.configuration.ArangoConfig;
import com.epam.arangoPractice.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class FamilyRepository {
    private final String GET_FAMILY_BY_KEY =
            "FOR member IN @@COLLECTION RETURN member";
    private final String GET_FAMILY_BY_NAME =
            "FOR member IN @@COLLECTION SORT member.name RETURN member";
    private final String GET_FAMILY_BY_BIRTH =
            "FOR member IN @@COLLECTION SORT member.birthDate RETURN member";
    private final String GET_MEMBER =
            "FOR member IN @@COLLECTION FILTER member.id == @@key RETURN member";
    private final String UPDATE_MEMBER =
            "LET newData = [@@member] FOR member IN @@COLLECTION UPDATE { _key: @@key} WITH newData IN @@COLLECTION";
    private ArangoDatabase aDb;
    private MapBuilder mapBuilder = new MapBuilder();
    private ArangoConfig config;

    @Autowired
    public FamilyRepository(ArangoConfig config) {
        this.config = config;
        this.aDb = config.getDb();
    }

    public List<Member> getFamily(String collectionName) {
        ArangoCursor<Member> cursor = aDb.query(GET_FAMILY_BY_KEY, mapBuilder.put("@COLLECTION", collectionName).get(), null, Member.class);
        return cursor.asListRemaining();
    }

    public List<Member> getFamilyByBirthdate(String collectionName) {
        ArangoCursor<Member> cursor = aDb.query(GET_FAMILY_BY_BIRTH, mapBuilder.put("@COLLECTION", collectionName).get(), null, Member.class);
        return cursor.asListRemaining();
    }

    public List<Member> getFamilyByName(String collectionName) {
        ArangoCursor<Member> cursor = aDb.query(GET_FAMILY_BY_NAME, mapBuilder.put("@COLLECTION", collectionName).get(), null, Member.class);
        return cursor.asListRemaining();
    }

    public Member getMember(String key, String collectionName) {
        ArangoCursor<Member> cursor = aDb.query(GET_MEMBER, mapBuilder.put("@key", key).put("@COLLECTION", collectionName).get(),
                null, Member.class);
        return cursor.next();
    }

    public Member modifyMember(Member member, String collectionName) {
        ArangoCursor<Member> cursor = aDb.query(UPDATE_MEMBER, mapBuilder
                        .put("@member", member)
                        .put("@key", member.getKey())
                        .put("@COLLECTION", collectionName).get(),
                null, Member.class);
        return cursor.next();
    }
}
