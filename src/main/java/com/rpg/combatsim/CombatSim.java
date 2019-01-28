package com.rpg.combatsim;

import com.rpg.combatsim.helper.SimGeneratorHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "com.rpg.combatsim")
@EnableJpaRepositories(basePackages = {"com.rpg.combatsim.repositories"})
public class CombatSim {
    public static void main(String[] args) {
        SpringApplication.run(CombatSim.class, args);

        SimGeneratorHelper simGeneratorHelper = new SimGeneratorHelper();
        simGeneratorHelper.genFFASim(simGeneratorHelper.generateBaseLindaList());

        Scanner sc = new Scanner(System.in);
        System.out.println("Again? (1 or 0)");
        int choice = sc.nextInt();
        while(choice != 0) {
            simGeneratorHelper.genFFASim(simGeneratorHelper.generateBaseLindaList());
            System.out.println("Again? (1 or 0)");
            choice = sc.nextInt();
        }
    }
}