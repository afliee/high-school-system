package com.highschool.highschoolsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class HighSchoolSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighSchoolSystemApplication.class, args);
    }

}
