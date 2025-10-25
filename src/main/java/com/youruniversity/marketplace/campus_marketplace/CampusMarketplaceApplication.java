package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
public class CampusMarketplaceApplication {

    private static final Logger log = LoggerFactory.getLogger(CampusMarketplaceApplication.class);

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called");
        return "OK";
    }

    public static void main(String[] args) {
        SpringApplication.run(CampusMarketplaceApplication.class, args);
    }

}
