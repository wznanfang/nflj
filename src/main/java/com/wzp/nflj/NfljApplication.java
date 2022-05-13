package com.wzp.nflj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class NfljApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfljApplication.class, args);
        System.out.println("---------------启动成功---------------");
    }

}
