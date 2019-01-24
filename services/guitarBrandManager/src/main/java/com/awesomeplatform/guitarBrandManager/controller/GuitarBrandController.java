package com.awesomeplatform.guitarBrandManager.controller;

import com.awesomeplatform.guitarBrandManager.model.GuitarBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GuitarBrandController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/brands")
    public GuitarBrand getBrands() {
        ResponseEntity<GuitarBrand> response = restTemplate.getForEntity("http://localhost:8080/guitars", GuitarBrand.class);
        return response.getBody();
    }
}
