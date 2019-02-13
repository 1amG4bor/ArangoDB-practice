package com.epam.arangoPractice.model;

public enum Gender {
    MALE("Male", "Mann", "Férfi"),
    FEMALE("Female", "Frau", "Nő");

    private String eng;
    private String de;
    private String hu;

    Gender(String eng, String de, String hu) {
        this.eng = eng;
        this.de = de;
        this.hu = hu;
    }

    public String getEng() {
        return eng;
    }

    public String getDe() {
        return de;
    }

    public String getHu() {
        return hu;
    }
}
