package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    public String getHelloWorld() {
        return "Hello World!";
    }

}
