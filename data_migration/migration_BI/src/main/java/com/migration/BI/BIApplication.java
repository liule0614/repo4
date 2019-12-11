package com.migration.BI;

import com.migration.util.RedisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BIApplication {

    public static void main(String[] args) {

        SpringApplication.run(BIApplication.class,args);

    }

    @Bean
    public RedisService redisService(){
        return new RedisService();
    }

}
