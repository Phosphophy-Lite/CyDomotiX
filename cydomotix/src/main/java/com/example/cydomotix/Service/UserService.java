package com.example.cydomotix.Service;

import com.example.cydomotix.Model.AccessType;
import com.example.cydomotix.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

        // Check if username isn't already taken by another registered user
        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Pseudonym already in use.");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccessType(AccessType.USER); // Default role for new users

        // Use a CrudRepository method to save the user object into the corresponding database
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
            existingUser.setPhoto(pfpUrl);  // Update profile picture link
            return userRepository.save(user); // Save changes
        } else {
            throw new RuntimeException("User not found with ID: " + user.getId());
        }
    }

}
