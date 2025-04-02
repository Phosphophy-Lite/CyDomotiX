package com.example.cydomotix.Service;

import com.example.cydomotix.Model.AccessType;
import com.example.cydomotix.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.cydomotix.Repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Vérifie si un pseudonyme donné n'est pas déjà utilisé dans la BDD
     * @param username
     * @return 1 : le pseudo figure dans la BDD / 0 : le pseudo n'existe pas dans la BDD
     */
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Enregistre un nouvel utilisateur dans la BDD
     * Vérifie que le pseudo donné n'existe pas déjà dans la BDD (pris par un autre utilisateur)
     * Chiffre le mot de passe et attribue au nouvel utilisateur les permissions de "USER" par défaut
     * @param user L'utilisateur temporaire qu'on essaie d'enregistrer dans la BDD
     * @return Un objet User de l'utilisateur enregistré avec succès dans la BDD
     */
    public User registerUser(User user) {
        System.out.println("Creating new user " + user.getUsername());

        // Vérifie que le pseudo n'est pas déjà utilisé par un autre utilisateur dans la BDD
        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Pseudonym already in use.");
        }

        // Chiffre le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccessType(AccessType.USER); // Default role for new users

        // Utilise une méthode de CrudRepository pour sauvegarder l'utilisateur dans la BDD
        return userRepository.save(user);
    }

    /**
     * Attribue le lien d'une photo de profile stockée sur le serveur à un utilisateur :
     * Récupère l'utilisateur de la BDD par son ID
     * Met à jour le lien de la photo de profil pour cet utilisateur
     * @param user l'utilisateur dont on veut changer la photo de profil
     * @param pfpUrl lien local de la photo de profil enregistrée sur le serveur
     * @return
     */
    public User setUserProfilePicture(User user, String pfpUrl){
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setPhoto(pfpUrl);  // Mets à jour le lien de l'image
            return userRepository.save(user); // Sauvegarde les changements
        } else {
            throw new RuntimeException("User not found with ID: " + user.getId());
        }
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Mettre à jour un utilisateur existant
     * @param id Identifiant de l'utilisateur
     * @param updatedUser Données du formulaire de l'utilisateur mis à jour avec les nouvelles valeurs
     */
    public void update(Authentication authentication, Integer id, User updatedUser) {
        userRepository.findById(id).ifPresent(existingUser -> {

            String newUsername = getUpdatedValue(updatedUser.getUsername(), existingUser.getUsername());
            boolean usernameChanged = !newUsername.equals(existingUser.getUsername());

            existingUser.setUsername(newUsername);
            existingUser.setGender(updatedUser.getGender());
            existingUser.setBirthDate(updatedUser.getBirthDate());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setPhoto(updatedUser.getPhoto());

            // Sauvegarde les modifications
            userRepository.save(existingUser);

            // Si l'username a changé, updater aussi le username de la session du context Spring Security
            if(usernameChanged){
                UserDetails updatedUserDetails = customUserDetailsService.loadUserByUsername(newUsername);

                // Create a new authentication token with updated details
                // Créer un nouveau token d'authentification dans le contexte de Spring Security avec les infos mises à jour
                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        updatedUserDetails,
                        authentication.getCredentials(),
                        updatedUserDetails.getAuthorities()
                );

                // Mettre à jour le contexte Spring Security
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        });
    }

    /**
     * Retourne une valeur mise à jour, ou conserve l'ancienne si la nouvelle est vide
     */
    private String getUpdatedValue(String newValue, String oldValue) {
        return (newValue != null && !newValue.trim().isEmpty()) ? newValue : oldValue;
    }

}
