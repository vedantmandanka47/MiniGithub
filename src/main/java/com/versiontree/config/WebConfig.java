package com.versiontree.config;

// SYLLABUS: Spring MVC - Resource Handling Configuration
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /static/** requests to classpath:/static/ and /static/ directory
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "/static/", "/WEB-INF/static/");
    }
}
