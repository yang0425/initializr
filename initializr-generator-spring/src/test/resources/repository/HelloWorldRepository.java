package com.example.demo.repository;

import com.example.demo.entity.HelloWorld;
import org.springframework.stereotype.Repository;

@Repository
public class HelloWorldRepository {

    public HelloWorld getHelloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setMessage("Hello World!");
        return helloWorld;
    }

}
