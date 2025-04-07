package com.example.cydomotix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DatabaseBackupService {

    @Autowired
    private DataSource dataSource;


    // Exécuté toutes les 4 heures
    @Scheduled(fixedRate = 4 * 60 * 60 * 1000) // 4h en millisecondes
    public void backupDatabase() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFile = "data/backup/backup_" + timestamp + ".sql";

        try {
            new File("data/backup").mkdirs(); // Crée le dossier s'il n'existe pas

            try (Connection connection = dataSource.getConnection();
                 Statement stmt = connection.createStatement()) {
                stmt.execute("SCRIPT TO '" + backupFile + "'");
                System.out.println("Automated database backup saved successfully : " + backupFile);
            }
        } catch (Exception e) {
            System.err.println("Database backup failed : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
