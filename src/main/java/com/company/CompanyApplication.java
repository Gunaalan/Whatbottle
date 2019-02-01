package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.company")
public class CompanyApplication {


    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);
    }

}

