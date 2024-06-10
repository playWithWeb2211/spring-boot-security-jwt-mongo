package org.spring.jwt.mongo.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.spring.jwt.mongo.project.*")
public class SpringBootSecurityJwtMongodbApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityJwtMongodbApplication.class,args);
    }
}
