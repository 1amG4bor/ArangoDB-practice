package com.epam.arangoPractice.model;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Member {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Gender gender;

    public Member() {
    }

    public Member(Long id, String name, Gender gender, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
