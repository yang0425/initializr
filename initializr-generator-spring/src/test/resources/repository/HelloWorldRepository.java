package com.example.demo.repository;

import org.springframework.stereotype.Repository;

@Repository
public class HelloWorldRepository {

    public String getHelloWorld() {
        return "Hello World!";
    }

}
