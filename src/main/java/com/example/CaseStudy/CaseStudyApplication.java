package com.example.CaseStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.CaseStudy")
public class CaseStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaseStudyApplication.class, args);
    }
}
