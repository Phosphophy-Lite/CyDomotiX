package com.example.cydomotix.Service;

import com.example.cydomotix.Model.User;
import com.example.cydomotix.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Retrieve user-related data from DB and encapsulate that data
 * for Spring Security to authenticate the user
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Needed for access to the database
    @Autowired
    private UserRepository userRepository;

    /**
     * Method called by Spring Security to retrieve user details
     * (through the login form configured in SecurityConfig)
     * Compares the password provided by the user during login with the encoded password in the database
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Authenticating user : " + username);

        // fetch user from DB by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("User not found : " + username);
                    return new UsernameNotFoundException("User not found");
                });

        System.out.println("User found: " + user.getUsername() + " with role: " + user.getAccessType());


        // Return Spring Security UserDetails object with the user's details
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // already encrypted
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getAccessType())) // Access rights (expl : "ROLE_USER")
        );
    }
}
