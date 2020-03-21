package com.caohao.filepan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.caohao.filepan.dao")
public class FilepanApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilepanApplication.class, args);
    }

}
