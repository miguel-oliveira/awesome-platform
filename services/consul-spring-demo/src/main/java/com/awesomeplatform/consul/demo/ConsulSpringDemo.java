package com.awesomeplatform.consul.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsulSpringDemo {

  public static void main(String[] args) {
    SpringApplication.run(ConsulSpringDemo.class, args);
  }

  @Bean
  public RestTemplate restTemplateFactory() {
    return new RestTemplate();
  }

}
