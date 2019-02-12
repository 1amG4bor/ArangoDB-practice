package com.epam.arangoPractice.service;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public String getHelloMessage() {
        return getHelloMessage("World");
    }

    public String getHelloMessage(String name) {
        return String.format("Hello %s", name);
    }
}