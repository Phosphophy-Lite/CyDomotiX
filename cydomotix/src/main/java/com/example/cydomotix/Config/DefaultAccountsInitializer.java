package com.example.cydomotix.Config;

import com.example.cydomotix.Model.Users.AccessType;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Initialise des comptes par défaut (admin, dev, user normal) pour showcase et tester
 */
@Component
public class DefaultAccountsInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            System.out.println("Initializing default accounts...");
            initDefaultAccounts();
        } else {
            System.out.println("Default accounts already initialized.");
        }
    }

    private void createDefaultUser(String username, String password, AccessType role){

        // Vérifier que l'utilisateur n'existe pas déjà
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            // Créer un nouvel utilisateur
            User usr = new User(
                    username,
                    passwordEncoder.encode(password),
                    role
            );

            usr.setEnabled(true);
            usr.setApprovedByAdmin(true);

            userRepository.save(usr);
            System.out.println("Initialized default " + usr.getAccessType() + " account with credentials : " + usr.getUsername() + "/" + password);

        } else{
            System.out.println(username + " already exists.");
        }
    }

    public void initDefaultAccounts() {
        createDefaultUser("admin", "adminpassword", AccessType.ADMIN);
        createDefaultUser("engineer", "devpassword", AccessType.DEV);
        createDefaultUser("crewmate", "userpassword", AccessType.USER);
    }
}
