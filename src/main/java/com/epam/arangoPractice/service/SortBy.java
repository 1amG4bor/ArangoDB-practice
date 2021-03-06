package com.epam.arangoPractice.service;

public enum SortBy {
    BY_BIRTH("birth"),
    BY_NAME("name");

    private String value;

    SortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
