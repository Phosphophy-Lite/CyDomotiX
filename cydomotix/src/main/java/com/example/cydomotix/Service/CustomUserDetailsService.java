package com.example.cydomotix.Service;

import com.example.cydomotix.Exceptions.EmailNotVerifiedException;
import com.example.cydomotix.Exceptions.NotApprovedByAdminException;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Repository.UserRepository;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Récupère les données relatives à un utilisateur de la BDD et encapsule ces données
 * pour que Spring Security authentifie l'utilisateur
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Méthode appelée par Spring Security pour récupérer les données utilisateur
     * grâce au formulaire de login configuré dans SecurityConfig
     * Compare le mot de passe donné par l'utilisateur dans le formulaire avec le mot de passe chiffré de la BDD
     * @param username Nom d'utilisateur envoyé par le formulaire de login
     * @return données utilisateur
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Authenticating user : " + username);

        // Récupère l'utilisateur de la BDD via son nom d'utilisateur
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("User not found");
                });

        // Vérifier si le compte n'a pas été approuvé par un admin
        if (!user.isApprovedByAdmin()) {
            throw new NotApprovedByAdminException();
        }

        // Vérifier si le compte n'a pas été vérifié par mail
        if (!user.isEnabled()) {
            throw new EmailNotVerifiedException();
        }

        System.out.println("User found: " + user.getUsername() + " with role: " + user.getAccessType());

        // Retourne un objet de Spring Security UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // chiffré
                user.isEnabled(),
                true,true,true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getAccessType())) // Permissions (expl : "ROLE_USER")
        );
    }
}
