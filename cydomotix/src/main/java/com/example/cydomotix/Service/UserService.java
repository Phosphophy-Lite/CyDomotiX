package com.example.cydomotix.Service;

import com.example.cydomotix.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.cydomotix.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User registerUser(User user) {
        System.out.println("Creating new user " + user.getUsername());

        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Pseudonym already in use.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccessType("USER"); // Default role for new users

        return userRepository.save(user);
    }

}
