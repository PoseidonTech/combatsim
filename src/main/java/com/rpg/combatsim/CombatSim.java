package com.rpg.combatsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.rpg.combatsim")
@EnableJpaRepositories(basePackages = {"com.rpg.combatsim.repositories"})
public class CombatSim {
    public static void main(String[] args) {
        SpringApplication.run(CombatSim.class, args);
    }
}