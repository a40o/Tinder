package com.volasoftware.tinder;

import com.volasoftware.tinder.services.AuditableService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

@SpringBootApplication
public class TinderApplication {

//    @Bean
//    public AuditorAware<String> appendable(){
//        return new AuditableService();
//    }

    public static void main(String[] args) {
        SpringApplication.run(TinderApplication.class, args);
    }

}
