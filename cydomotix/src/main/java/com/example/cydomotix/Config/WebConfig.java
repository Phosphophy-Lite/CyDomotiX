package com.example.cydomotix.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Désactive le cache des images profil pour qu'elles soient instantanément mises à jour quand elles sont changées dans le profil
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/profilePictures/**")
                .addResourceLocations("file:src/main/resources/static/img/profilePictures/")
                .setCachePeriod(0); // Disable caching
    }
}
