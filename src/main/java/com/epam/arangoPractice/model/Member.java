package com.epam.arangoPractice.model;

import com.arangodb.entity.DocumentEntity;

public class Member extends DocumentEntity {
    private String name;
    private String birthDate;
    private Gender gender;

    public Member() { }

    public Member(String name, String birthDate, Gender gender) {
        super();
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return this.gender;
    }

}
