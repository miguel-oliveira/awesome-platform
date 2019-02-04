package com.awesomeplatform.consul.demo.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

// Properties will be updated when consul Key Value stores are updated
@RefreshScope
@Configuration
// Bind this Object to some app property prefix
@ConfigurationProperties("my.custom.prop")
public class Properties {

  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
