package com.awesomeplatform.consul.demo.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.naming.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/consul/demo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsulDiscoveryClientController {

  @Value("${spring.application.name}")
  private String serviceName;

  @Value("${my.custom.prop.value}")
  private String staticPropValue;

  private DiscoveryClient discoveryClient;

  private RestTemplate restTemplate;

  private Properties properties;

  private SecretProperties secretProperties;

  @Autowired
  public void setDiscoveryClient(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  @Autowired
  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Autowired
  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  @Autowired
  public void setSecretProperties(SecretProperties secretProperties) {
    this.secretProperties = secretProperties;
  }

  // Returns a list of available services
  @GetMapping("/services")
  public ResponseEntity<List<String>> getServices() {
    return ResponseEntity.ok(discoveryClient.getServices());
  }

  // Pings this service through dynamic service discovery
  @GetMapping("/discovery")
  public ResponseEntity<String> discovery() throws ServiceUnavailableException {
    return restTemplate.getForEntity(
        serviceUrl().orElseThrow(ServiceUnavailableException::new).resolve("/consul/demo/ping"),
        String.class);
  }

  private Optional<URI> serviceUrl() {
    return discoveryClient.getInstances(serviceName).stream().map(ServiceInstance::getUri)
        .findFirst();
  }

  // Ping endpoint used by consul agent as health check and by this app when dynamically discovering
  // the service
  @GetMapping("/ping")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("pong");
  }

  // Gets our dynamic custom property value stored in Consul's Key/Value store, this value will
  // change with consul
  @GetMapping("/configuration/dynamic")
  public ResponseEntity<String> getConfigurationDynamic() {
    return ResponseEntity.ok(properties.getValue());
  }

  // Gets our static custom property value that was loaded when app started, this value will not
  // change with consul
  @GetMapping("/configuration/static")
  public ResponseEntity<String> getConfigurationStatic() {
    return ResponseEntity.ok(staticPropValue);
  }

  @GetMapping("/secret")
  public ResponseEntity<String> getSecret() {
    return ResponseEntity.ok(secretProperties.getToken());
  }

}
