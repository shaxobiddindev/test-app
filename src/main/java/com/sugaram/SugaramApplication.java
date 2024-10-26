package com.sugaram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SugaramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SugaramApplication.class, args);
    }

}
