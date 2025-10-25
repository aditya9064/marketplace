package com.youruniversity.marketplace.campus_marketplace;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void configurePathMatch(org.springframework.web.servlet.config.annotation.PathMatchConfigurer configurer) {
        log.info("Configuring path matching");
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Configuring CORS");
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:8082")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
            .allowedHeaders("*")
            .exposedHeaders("Content-Type", "Date", "Total-Count", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
            .maxAge(3600)
            .allowCredentials(true);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Configuring resource handlers");
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:uploads/");
    }

    @Override
    public void configureContentNegotiation(org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer configurer) {
        log.info("Configuring content negotiation");
        configurer.favorParameter(false)
            .favorPathExtension(false)
            .ignoreAcceptHeader(false)
            .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON);
    }
}
