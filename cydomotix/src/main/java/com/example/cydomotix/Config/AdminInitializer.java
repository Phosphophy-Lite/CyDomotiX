package com.example.cydomotix.Config;

import com.example.cydomotix.Model.User;
import com.example.cydomotix.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Initializes a default Admin account for project showcase & testing purposes
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Check if an admin user already exists
        Optional<User> adminUser = userRepository.findByUsername("admin");

        if (adminUser.isEmpty()) {
            // Create a new admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpassword")); // Encrypt password
            admin.setAccessType("ADMIN"); // sets authority to ADMIN

            userRepository.save(admin); // Save the admin user in DB
            System.out.println("Default admin user created: admin/adminpassword");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
