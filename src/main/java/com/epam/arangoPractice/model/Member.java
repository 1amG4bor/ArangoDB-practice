package com.epam.arangoPractice.model;

public class Member {
    private String _key;
    private String name;
    private String birthDate;
    private Gender gender;

    public Member() { }

    public Member(String _key, String name, String birthDate, Gender gender) {
        this._key = _key;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getKey() {
        return _key;
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
