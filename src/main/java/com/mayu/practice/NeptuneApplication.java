package com.mayu.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NeptuneApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeptuneApplication.class, args);

    }
}
