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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.web.servlet.function.RequestPredicates.headers;


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
                .authorizeHttpRequests(auth -> {

                    // Allow H2 console only in dev mode
                    if ("dev".equals(System.getProperty("spring.profiles.active", "dev"))) {
                        auth.requestMatchers("/h2-console/**", "/h2-console").permitAll();
                    }

                    auth.requestMatchers("/", "/register", "/css/**", "/js/**", "/img/**").permitAll(); // Public pages (no authentication required)
                    auth.requestMatchers("/admin/**").hasRole("ADMIN"); // Pages in /admin/ are restricted to ADMIN users


                    auth.anyRequest().authenticated(); // All other requests need authentication
                })
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
                )
                .csrf(csrf -> csrf.disable()) //disable csrf protection ; not recommended for prod. H2 console doesn't work with CSRF enabled.
                .headers(headers -> headers.frameOptions((frameOptions) -> frameOptions.disable())); // Can be necessary to properly display H2-Console

        // Manage user sessions
        http.sessionManagement(session -> session
                .invalidSessionUrl("/login?expired=true") // Redirect if user tries to access protected page with invalid session (expired ID or manually deleted cookie)
                .maximumSessions(1) // Limit to one session per user
                .expiredUrl("/login?session-expired=true") // Redirect when session is now expired (inactivity/timeout)
        );

        // Handle
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/dashboard"); // Redirect to dashboard if access is denied
                })
        );

        return http.build();
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
