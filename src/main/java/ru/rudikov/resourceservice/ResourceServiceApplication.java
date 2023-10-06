package ru.rudikov.resourceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(exclude = {ReactiveUserDetailsServiceAutoConfiguration.class})
@EnableKafka
public class ResourceServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResourceServiceApplication.class, args);
  }
}
