package com.ashish.monopoly;

import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class Monopoly {
    public static void main(String[] args) {
        SpringApplication.run(Monopoly.class, args);
    }

}
