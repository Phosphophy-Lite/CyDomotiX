package com.example.cydomotix.Config;

import com.example.cydomotix.Model.AccessType;
import com.example.cydomotix.Model.User;
import com.example.cydomotix.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Initializes a default Admin account for project showcase & testing purposes
 * And a default Dev account
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private void createPriviledgedUser(String username, String password, AccessType role){

        // Check if user already exists
        Optional<User> priviledgedUser = userRepository.findByUsername(username);

        if (priviledgedUser.isEmpty()) {
            // Create a new user
            User usr = new User(
                    username,
                    passwordEncoder.encode(password),
                    role
            );

            userRepository.save(usr);
            System.out.println("Initialized admin account with credentials : " + usr.getUsername() + "/" + password);

        }else{
            System.out.println(username + "already exists.");
        }
    }

    @Override
    public void run(String... args) {

        createPriviledgedUser("admin", "adminpassword", AccessType.ADMIN);
        createPriviledgedUser("dev", "devpassword", AccessType.DEV);

    }
}
