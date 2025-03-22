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
public class DefaultAccountsInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private void createDefaultUser(String username, String password, AccessType role){

        // Check if user already exists
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            // Create a new user
            User usr = new User(
                    username,
                    passwordEncoder.encode(password),
                    role
            );

            userRepository.save(usr);
            System.out.println("Initialized default account with credentials : " + usr.getUsername() + "/" + password);

        }else{
            System.out.println(username + "already exists.");
        }
    }

    @Override
    public void run(String... args) {

        createDefaultUser("admin", "adminpassword", AccessType.ADMIN);
        createDefaultUser("dev", "devpassword", AccessType.DEV);
        createDefaultUser("user", "userpassword", AccessType.USER);

    }
}
