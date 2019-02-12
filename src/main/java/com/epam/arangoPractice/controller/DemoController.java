package com.epam.arangoPractice.controller;

import com.epam.arangoPractice.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/hello")
    public String hello() {
        return demoService.getHelloMessage();
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return demoService.getHelloMessage(name);
    }
}
