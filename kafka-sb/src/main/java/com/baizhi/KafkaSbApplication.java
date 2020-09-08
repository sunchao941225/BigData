package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaSbApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSbApplication.class, args);
    }

}
