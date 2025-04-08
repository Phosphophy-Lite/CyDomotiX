package com.example.cydomotix.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() throws SQLException {
        if (!tableExists("Users")) {
            System.out.println("Missing tables. Loading schema.sql...");

            try (Connection conn = dataSource.getConnection()) {
                // Exécution du schéma
                ScriptUtils.executeSqlScript(conn, new ClassPathResource("schema.sql"));
                System.out.println("Schema loaded successfully.");


                // Exécution des données initiales
                ScriptUtils.executeSqlScript(conn, new ClassPathResource("data.sql"));
                System.out.println("Initial data loaded successfully.");
            } catch (Exception e) {
                System.err.println("Error while loading schema.sql : " + e.getMessage());
            }
        } else {
            System.out.println("Database already initialized. Nothing to do.");
        }
    }

    private boolean tableExists(String tableName) {
        try {
            jdbcTemplate.queryForObject("SELECT 1 FROM " + tableName + " LIMIT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

