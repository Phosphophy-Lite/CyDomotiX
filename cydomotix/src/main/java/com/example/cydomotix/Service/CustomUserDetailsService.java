package com.example.cydomotix.Service;

import com.example.cydomotix.Model.User;
import com.example.cydomotix.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Retrieve user-related data and encapsulate that data
 * for Spring Security to authenticate the user
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Needed for access to the database
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        // fetch user from DB by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Return Spring Security UserDetails object with the user's details
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // already encoded
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getAccessType())) // Access rights (expl : "ROLE_USER")
        );
    }
}
