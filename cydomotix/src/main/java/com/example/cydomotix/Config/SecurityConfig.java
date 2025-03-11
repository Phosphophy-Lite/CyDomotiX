package com.example.cydomotix.Config;

import com.example.cydomotix.Repository.UserRepository;
import com.example.cydomotix.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Configures authentication and authorization for the web app
 */
@EnableWebSecurity // Imports HttpSecurityConfiguration
@Configuration // config automatically loaded when app launched
public class SecurityConfig {

    /**
     * Defines :
     * - Which pages are accessible to certain types of users
     * - Which pages require authentication.
     * - How users authenticate (login, logout, encryption)
     * Session management and CSRF protection
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register").permitAll() // Public pages (no authentication require)
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Pages in /admin/ are restricted to ADMIN users
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .formLogin(login -> login // Configures form-based login
                        .loginPage("/login") // Redirects users to /login for authentication
                        .defaultSuccessUrl("/dashboard", true) // Redirect to dashboard page after login
                        .permitAll()
                        .failureHandler((request, response, exception) -> { // If credentials are invalid, give a feedback to the user
                            if (exception instanceof BadCredentialsException) {
                                request.setAttribute("errorMessage", "Username or password invalid");
                            } else {
                                request.setAttribute("errorMessage", "Login failed");
                            }
                            response.sendRedirect("/login?error=true"); // Redirect back to login page with error message
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Custom logout URL
                        .logoutSuccessUrl("/") // Redirect to home page after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // Delete the session cookie
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Used by Spring Security to load user details during authentication
     * @param userRepository
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    /**
     * Uses BCryptPasswordEncoder to encode passwords before storing them in the database
     * @return encoded password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
