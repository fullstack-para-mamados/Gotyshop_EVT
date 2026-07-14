package com.example.GotyStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GotyStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(GotyStoreApplication.class, args);
    }
}
