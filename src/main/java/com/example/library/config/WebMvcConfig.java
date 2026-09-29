package com.example.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig
        implements WebMvcConfigurer {

    @Value("${application.upload-dir}")
    private String uploadDirectory;

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {

        String uploadPath =
                "file:" + uploadDirectory + "/";

        registry.addResourceHandler(
                "/uploads/**"
        )
        .addResourceLocations(
                uploadPath
        );

    }

}