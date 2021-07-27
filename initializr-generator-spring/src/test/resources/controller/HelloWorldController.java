package com.example.demo.controller;

import com.example.demo.dto.HelloWorldDto;
import com.example.demo.service.HelloWorldService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("helloWorld")
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping
    public HelloWorldDto helloWorld() {
        return helloWorldService.getHelloWorld();
    }

}
