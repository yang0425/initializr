package com.example.demo.service;

import com.example.demo.dto.HelloWorldDto;
import com.example.demo.entity.HelloWorld;
import com.example.demo.repository.HelloWorldRepository;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    private final HelloWorldRepository helloWorldRepository;

    public HelloWorldService(HelloWorldRepository helloWorldRepository) {
        this.helloWorldRepository = helloWorldRepository;
    }

    public HelloWorldDto getHelloWorld() {
        HelloWorld message = helloWorldRepository.getHelloWorld();

        return new HelloWorldDto(message.getMessage());
    }

}
