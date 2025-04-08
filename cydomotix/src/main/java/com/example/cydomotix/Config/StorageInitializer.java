package com.example.cydomotix.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StorageInitializer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.storage.base-dir}")
    private String baseDir;

    @PostConstruct
    public void init() {
        createIfNotExists(uploadDir);
        createIfNotExists(baseDir + "/data"); // Pour la BDD
    }

    private void createIfNotExists(String pathStr) {
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Creating directory : " + path);
            } catch (IOException e) {
                throw new RuntimeException("Cannot create directory : " + path, e);
            }
        }
    }
}
