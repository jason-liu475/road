package org.liu.road;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@MapperScan("org.liu.road.dao")
@SpringBootApplication
public class RoadApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadApplication.class, args);
    }

}
