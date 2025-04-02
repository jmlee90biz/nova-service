package com.sktelecom.nova.sba.partner;

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
public class NovaServicePartnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaServicePartnerApplication.class, args);
    }

}
