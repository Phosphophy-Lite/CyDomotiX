package com.example.cydomotix.Config;

import com.example.cydomotix.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre la connexion de sessions gardées en mémoire de comptes qui n'existent plus.
 * Exemple : quand on était connecté via un compte nouvellement créé et qu'on arrête le serveur sans se déconnecter,
 * en relançant le serveur, la session existe toujours et l'utilisateur est connecté,
 * mais les données de son compte n'existent plus (BDD réinitialisée quand on relance)
 * Donc il faut déconnecter cette session.
 */
@Component
public class UserExistenceFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();

            // Check if the user still exists in the database
            if (!userRepository.findByUsername(username).isPresent()) {
                System.out.println("User no longer exists. Logging out.");

                // Invalidate session
                request.getSession().invalidate();
                SecurityContextHolder.clearContext();

                // Redirect to login page
                response.sendRedirect("/login?session-expired=true");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
