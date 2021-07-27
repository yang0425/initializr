package com.example.demo.service;

import com.example.demo.repository.HelloWorldRepository;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    private final HelloWorldRepository helloWorldRepository;

    public HelloWorldService(HelloWorldRepository helloWorldRepository) {
        this.helloWorldRepository = helloWorldRepository;
    }

    public String getHelloWorld() {
        return helloWorldRepository.getHelloWorld();
    }

}
