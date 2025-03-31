package com.example.cydomotix.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

/**
 * Configure l'authentification et les autorisations de l'application web
 * Gère les sessions utilisateur, login et logout
 */
@EnableWebSecurity // Importe HttpSecurityConfiguration
@Configuration // config automatiquement chargée au lancement de l'application
public class SecurityConfig {

    @Autowired
    private Environment environment; // pour accéder à la propriété de l'appli des profils actifs

    /**
     * Vérifier si un profil d'execution de l'application est actif dans les propriétés de l'application
     * @param profile - Le nom du profile
     */
    private boolean isProfileActive(String profile) {
        final String[] profiles = environment.getActiveProfiles();
        return Arrays.asList(profiles).contains(profile);
    }


    /**
     * Définit la hiérarchie des rôles d'utilisateurs
     * Rôle les plus supérieurs obtiennent toutes les permissions des rôles plus inférieurs
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_DEV > ROLE_ADMIN > ROLE_USER");
    }

    /**
     * Définit :
     * - Which pages are accessible to certain types of users
     * - Quelles pages sont accessibles à quel type d'utilisateur
     * - Quelles pages nécessitent une authentification
     * - Comment les utilisateurs s'authentifient (login, logout, chiffrement...)
     * - Gestion d'une session utilisateur
     * @param http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> {

                    // A SUPPRIMER POUR PROD
                    if (isProfileActive("dev")) {
                        auth.requestMatchers("/h2-console/**", "/h2-console").hasRole("ADMIN");
                    } else {
                        auth.requestMatchers("/h2-console/**", "/h2-console").denyAll();
                    }

                    auth.requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/img/**", "/error").permitAll(); // Pages publiques (pas besoin d'authentification)
                    auth.requestMatchers("/admin/**").hasRole("ADMIN"); // Pages dans /admin/ sont restreintes aux rôles ADMIN et supérieurs
                    auth.requestMatchers("/dev/**").hasRole("DEV"); // Pages dans /dev/ sont restreintes aux rôles DEV et supérieurs

                    auth.anyRequest().authenticated(); // Toute autre requête nécessite authentification
                })
                .formLogin(login -> login // Configure un login via formulaire
                        .loginPage("/login") // Redirige les utilisateurs vers /login pour s'authentifier
                        .defaultSuccessUrl("/dashboard", true) // Redirige vers /dashboard après authentification
                        .permitAll()
                        .failureHandler((request, response, exception) -> { // Si username/password invalides, afficher les erreurs
                            if (exception instanceof BadCredentialsException) {
                                request.getSession().setAttribute("errorMessage", "Pseudonyme ou mot de passe invalide.");
                            } else {
                                request.getSession().setAttribute("errorMessage", "Échec de connexion. Veuillez réessayer.");
                            }
                            response.sendRedirect("/login?error=true"); // Redirige les erreurs vers la page de connexion
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL déconnexion
                        .logoutSuccessUrl("/") // Redirige vers la page d'accueil après déconnexion
                        .invalidateHttpSession(true) // Ferme la session
                        .deleteCookies("JSESSIONID") // Supprime le cookie de session
                )
                .headers(headers -> headers.frameOptions((frameOptions) -> frameOptions.disable())); // Peut être nécessaire pour afficher correctement H2-Console

        // Désactive csrf protection ; par recommendé pour prod mais nécessaire en mode dev car H2 console ne marche pas avec csrf activé.
        if (isProfileActive("dev")) {
            http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        }

        // Gère les session utilisateur
        http.sessionManagement(session -> session
                .invalidSessionUrl("/login?expired=true") // Redirige si l'utilisateur tente d'accéder à une page protégée avec une session invalide (expirée ou cookie supprimé manuellement)
                .maximumSessions(1) // Limiter à une session par utilisateur
                .expiredUrl("/login?session-expired=true") // Rediriger quand la session est expirée (timeout/logout)
        );

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/error") // Rediriger les autres erreurs/pages protégées sans permissions vers page d'erreur custom
        );

        return http.build();
    }

    /**
     * Utilise BCryptPasswordEncoder pour chiffrer les mots de passe avant de les stocker en BDD
     * @return mot de passe chiffré
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
