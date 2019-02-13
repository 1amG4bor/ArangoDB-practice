package com.epam.arangoPractice.service;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.epam.arangoPractice.configuration.ArangoConfig;
import com.epam.arangoPractice.model.Gender;
import com.epam.arangoPractice.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FamilyService {
    private final String collectionName = "Family";
    private ArangoConfig arangoConfig;
    private ArangoDatabase aDB;

    @Autowired
    public FamilyService(ArangoConfig arangoConfig) {
        this.arangoConfig = arangoConfig;
    }

    @PostConstruct
    private void initIt() {
        if (arangoConfig.createCollection(collectionName)) {
            aDB = arangoConfig.getDb();
            ArangoCollection familyCollection = aDB.collection(collectionName);
            try {
                familyCollection.insertDocument(new Member(1L, "Iza", Gender.FEMALE, LocalDate.of(1988, 2, 18)));
                familyCollection.insertDocument(new Member(2L, "Maja", Gender.FEMALE, LocalDate.of(2014, 3, 17)));
                familyCollection.insertDocument(new Member(3L, "Dad", Gender.MALE, LocalDate.of(1941, 6, 6)));
                familyCollection.insertDocument(new Member(4L, "Mom", Gender.FEMALE, LocalDate.of(1944, 2, 2)));
                familyCollection.insertDocument(new Member(5L, "Peter", Gender.MALE, LocalDate.of(1976, 6, 21)));
                familyCollection.insertDocument(new Member(6L, "Daniel", Gender.MALE, LocalDate.of(1978, 10, 30)));
                familyCollection.insertDocument(new Member(0L, "Me", Gender.MALE, LocalDate.of(1981, 8, 7)));
            } catch (Exception e) {
                log.error("Fill up the database was not successful! Error: {}", e.getMessage());
            }
        }
    }

    public Set<Member> getFamily() {
        ArangoCursor<BaseDocument> cursor = aDB.query("FOR f IN Family RETURN f", null, null, BaseDocument.class);
        return parseCursorToMemberSet(cursor.asListRemaining());
    }

    public Set<Member> getFamilyByBirthdate() {
        ArangoCursor<BaseDocument> cursor = aDB.query("FOR f IN Family SORT f.birthDate.year, f.birthDate.month, f.birthDate.day RETURN f",
                null, null, BaseDocument.class);
        return parseCursorToMemberSet(cursor.asListRemaining());
//        return new ArrayList<>(new HashSet<>(familyMap.values()))
//                .stream().sorted(Comparator.comparing(Member::getBirthDate))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//        return null;
    }

    public Set<Member> getFamilyByName() {
        ArangoCursor<BaseDocument> cursor = aDB.query("FOR f IN Family SORT f.name RETURN f", null, null, BaseDocument.class);
        return parseCursorToMemberSet(cursor.asListRemaining());
//        return new ArrayList<>(new HashSet<>(familyMap.values()))
//                .stream().sorted(Comparator.comparing(Member::getName))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//        return null;
    }

    public Member getMember(Long id) {
        Member member = null;
        try {
            ArangoCursor<BaseDocument> cursor = aDB.query("FOR f IN Family  FILTER f.id == " + id + " RETURN f", null, null, BaseDocument.class);
            member = parseBaseDocumentToMember(cursor.next());
        } catch (ArangoDBException e) {
            log.error("Failed to get member my id '{}'. Error: {}", id, e.getErrorMessage());
        }
        return member;
//        return familyMap.get(id);
    }

    public Member addMember(Member member) {
        member.setId(new Random().nextLong());
        try {
            aDB.collection(collectionName).insertDocument(member);
        } catch (ArangoDBException e) {
            log.error("Failed to add member named '{}'. Error: ", member.getName(), e.getErrorMessage());
        }
//        return familyMap.put(member.getId(), member);
        return getMember(member.getId());
    }

    public Member saveMember(Member member) {
        try {
            ArangoCursor<BaseDocument> cursor = aDB.query("FOR f IN Family  FILTER f.id == " + member.getId() + " RETURN f", null, null, BaseDocument.class);
            BaseDocument document = cursor.next();
            document.addAttribute("name", member.getName());
            document.addAttribute("birthDate", member.getBirthDate());
            document.addAttribute("gender", member.getGender());
            aDB.collection(collectionName).updateDocument(document.getKey(), document);
        } catch (ArangoDBException e) {
            log.error("Failed to save the member named '{}'. Error: {}", member.getName(), e.getErrorMessage());
        }
//        if (familyMap.containsKey(member.getId())) {
//            familyMap.put(member.getId(), member);
//            return familyMap.get(member.getId());
//        }
//        // TODO: throw notFound-exception
        return getMember(member.getId());
    }

    public boolean deleteMember(Long id) {
        try{
            ArangoCursor<BaseDocument> cursor = aDB.query("FOR f IN Family  FILTER f.id == " + id + " RETURN f", null, null, BaseDocument.class);
            String key = cursor.next().getKey();
            aDB.collection(collectionName).deleteDocument(key);
        } catch (ArangoDBException e) {
            log.error("Failed to delete document with id '{}'. Error: {}", id, e.getErrorMessage());
            return false;
        }
        return true;

//        // TODO: throw notFound-exception
//        if (!familyMap.containsKey(id)) throw new RuntimeException("Resource not found!");
//                familyMap.remove(id);
    }

    private BaseDocument parseMemberToDoc(Member member) {
        BaseDocument newItem = new BaseDocument();
        newItem.addAttribute("id", member.getId());
        newItem.addAttribute("name",   member.getName());
        newItem.addAttribute("birthDate",  member.getBirthDate());
        newItem.addAttribute("gender", member.getGender());
        return newItem;
    }


    private Set<Member> parseCursorToMemberSet(List<BaseDocument> baseDocumentList) {
        Set<Member> resultSet = new LinkedHashSet<>();

        for (BaseDocument item: baseDocumentList) {
            Member member = parseBaseDocumentToMember(item);
            resultSet.add(member);
        }
        return resultSet;
    }

    private Member parseBaseDocumentToMember(BaseDocument document) {
        HashMap<String, Object> map = (HashMap<String, Object>) document.getProperties();
        Member member = new Member();
        member.setId((Long) map.get("id"));
        member.setName((String) map.get("name"));
        HashMap<String, Long> birthMap = (HashMap<String, Long>) map.get("birthDate");
        member.setBirthDate(LocalDate.of(birthMap.get("year").intValue(), birthMap.get("month").intValue(), birthMap.get("day").intValue()));
        member.setGender((Gender.valueOf((String) map.get("gender"))));
        return member;
    }

}
