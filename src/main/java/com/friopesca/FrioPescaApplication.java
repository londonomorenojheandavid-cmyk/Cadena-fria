package com.friopesca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.friopesca")
@EntityScan(basePackages = "com.friopesca.model")
@EnableJpaRepositories(basePackages = "com.friopesca.repository")
public class FrioPescaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrioPescaApplication.class, args);
    }
}
