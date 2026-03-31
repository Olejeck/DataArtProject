package com.ai_project.dataart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Тепер за адресою /api/files/download/ім'я.jpg будуть доступні файли з папки uploads
        registry.addResourceHandler("/api/files/download/**")
                .addResourceLocations("file:uploads/");
    }
}