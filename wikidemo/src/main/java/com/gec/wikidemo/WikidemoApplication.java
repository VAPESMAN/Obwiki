package com.gec.wikidemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("com.gec.wikidemo.mapper")
@SpringBootApplication
public class WikidemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikidemoApplication.class, args);
    }

}
