package com.epam.arangoPractice.service;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.epam.arangoPractice.Repository.FamilyRepository;
import com.epam.arangoPractice.configuration.ArangoConfig;
import com.epam.arangoPractice.model.Gender;
import com.epam.arangoPractice.model.Member;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FamilyService {
    @Value("${arangodb.nodeCollections}")
    private String collectionName;

    private ArangoConfig config;
    private FamilyRepository repository;
    private ArangoDatabase aDB;
    private ArangoCollection familyCollection;

    @Autowired
    public FamilyService(ArangoConfig config, FamilyRepository repository) {
        this.config = config;
        this.repository = repository;
        this.aDB = config.getDb();
        familyCollection = config.getCollection(collectionName);
    }

    public List<Member> getFamily() {
        List<Member> family = repository.getFamily(collectionName);
        return family;
    }

    public List<Member> getFamily(Gender gender) {
        List<Member> family = getFamily();
        return family.stream().filter(member -> member.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public List<Member> getSortedFamily(SortBy sortBy) {
        switch (sortBy) {
            case BY_NAME:
                List<Member> familyByName = getFamilyByName();
                return familyByName;
            case BY_BIRTH:
                List<Member> familyByBirthdate = getFamilyByBirthdate();
                return familyByBirthdate;
        }
        return getFamily();
    }

    private List<Member> getFamilyByName() {
        return repository.getFamilyByName(collectionName);
    }

    private List<Member> getFamilyByBirthdate() {
        return repository.getFamilyByBirthdate(collectionName);
    }

    public Member getMember(String key) {
        Member member = repository.getMember(key, collectionName);
        return member;
    }

    public void addMember(Member member) {
        try {
            aDB.collection(collectionName).insertDocument(member);
        } catch (ArangoDBException e) {
            log.error("Failed to add member named '{}'. Error: ", member.getName(), e.getErrorMessage());
            // todo: throw new SomeSpecialException("Failed to add the member!");
        }
    }

    public void addAll(String jsonString) {
        jsonString = jsonString.replaceAll("\\s+","");
        Gson gson = new Gson();
        List<Member> memberList = Arrays.stream(gson.fromJson(jsonString, Member[].class)).collect(Collectors.toList());
        memberList.forEach(this::addMember);
    }

    public void modifyMember(Member member) {
        aDB.collection(collectionName).updateDocument(member.getKey(), member);
    }

    public void deleteMember(String key) {
        try {
            aDB.collection(collectionName).deleteDocument(key);
            log.info("Member has deleted with key: '{}'", key);
        } catch (ArangoDBException e) {
            log.error("Failed to delete document with key '{}'. Error: {}", key, e.getErrorMessage());
            // todo: throw new SomeSpecialException("Failed to delete the member with key: '" + key + "'!");
        }
    }
}
