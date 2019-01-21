package com.awesomeplatform.serviceTwo.controller;

import com.awesomeplatform.serviceTwo.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetingController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/greeting")
    public Greeting greeting() {
        ResponseEntity<Greeting> response = restTemplate.getForEntity("http://localhost:8080/greeting", Greeting.class);
        return response.getBody();
    }
}
