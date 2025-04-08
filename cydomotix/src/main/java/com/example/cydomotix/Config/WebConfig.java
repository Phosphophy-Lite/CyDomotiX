package com.example.cydomotix.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Désactive le cache des images profil pour qu'elles soient instantanément mises à jour quand elles sont changées dans le profil
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/profilePictures/**")
                .addResourceLocations("file:" + uploadDir + "/", "classpath:/static/img/profilePictures/")
                .setCachePeriod(0); // Disable caching
    }
}
