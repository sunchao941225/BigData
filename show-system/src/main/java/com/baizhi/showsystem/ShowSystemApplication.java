package com.baizhi.showsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baizhi.showsystem.dao")
public class ShowSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowSystemApplication.class, args);
    }

}
