package com.sktelecom.nova.service.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
        "com.sktelecom.nova"
})
@EnableJpaRepositories(basePackages = {
        "com.sktelecom.nova"
})
@SpringBootApplication(scanBasePackages = {
        "com.sktelecom.nova"
})
public class NovaServiceMonolithApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaServiceMonolithApplication.class, args);
    }

}
