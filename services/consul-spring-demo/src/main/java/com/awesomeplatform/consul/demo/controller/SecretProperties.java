package com.awesomeplatform.consul.demo.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// Getting the token used by consul from vault secrets
@Configuration
@ConfigurationProperties("spring.cloud.consul")
public class SecretProperties {

  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
