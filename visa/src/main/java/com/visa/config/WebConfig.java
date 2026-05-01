package com.visa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String frontendExternalBaseUrl;

    public WebConfig(@Value("${frontend.externalBaseUrl:http://localhost:5173}") String frontendExternalBaseUrl) {
        this.frontendExternalBaseUrl = frontendExternalBaseUrl;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                // 1) dossier d'exécution (./uploads/...) — utilisé par l'upload runtime
                .addResourceLocations("file:uploads/", "file:./uploads/")
                // 2) fallback dev si des fichiers existent dans les resources
                .addResourceLocations("classpath:/static/uploads/", "file:src/main/resources/static/uploads/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(frontendExternalBaseUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(false);
    }
}