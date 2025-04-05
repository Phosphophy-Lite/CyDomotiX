package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Users.AccessType;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Model.Users.VerificationToken;
import com.example.cydomotix.Repository.VerificationTokenRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.cydomotix.Repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private EmailService emailService;

    /**
     * Vérifie si un pseudonyme donné n'est pas déjà utilisé dans la BDD
     * @param username
     * @return 1 : le pseudo figure dans la BDD / 0 : le pseudo n'existe pas dans la BDD
     */
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
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
            throw new IllegalArgumentException("Username already in use.");
        }

        // Vérifie que le pseudo n'est pas déjà utilisé par un autre utilisateur dans la BDD
        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        // Chiffre le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccessType(AccessType.USER); // Default role for new users
        user.setEnabled(false);

        // Utilise une méthode de CrudRepository pour sauvegarder l'utilisateur dans la BDD
        User savedUser = userRepository.save(user);

        // Envoie l'email de vérification du compte
        sendVerificationToken(savedUser);

        return savedUser;
    }

    public void sendVerificationToken(User user){
        // Création du token de vérification
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // expire après 24h

        // Enregistrement dans la BDD
        verificationTokenRepository.save(verificationToken);

        // Envoie l'email de vérification du compte
        emailService.sendVerificationEmail(user, token);
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
            existingUser.setFirstName(getUpdatedValue(updatedUser.getFirstName(), existingUser.getFirstName()));
            existingUser.setLastName(getUpdatedValue(updatedUser.getLastName(), existingUser.getLastName()));
            existingUser.setEmail(getUpdatedValue(updatedUser.getEmail(), existingUser.getEmail()));

            // Sauvegarde les modifications
            userRepository.save(existingUser);

            // Si l'username a changé, updater aussi le username de la session du context Spring Security
            if(usernameChanged){
                UserDetails updatedUserDetails = customUserDetailsService.loadUserByUsername(newUsername);

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

    public String saveProfilePicture(MultipartFile file, Integer userId) {
        try {
            // Vérifier si le fichier est vide
            if (file == null || file.isEmpty()) {
                System.out.println("Error : No file received.");
                return null;
            }
            // Créer un répertoire si inexistant
            String uploadDir = "src/main/resources/static/img/profilePictures/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("Created new directory: " + uploadDir);
            }

            // Vérifier et récupérer l'extension du fichier
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                System.out.println("Error : invalid file extension.");
                return null;
            }

            // Générer un nom de fichier unique
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String uniqueFileName = "profile_" + userId + fileExtension;
            File outputFile = new File(uploadDir + uniqueFileName);

            // Compression de l'image avec Thumbnailator
            InputStream inputStream = file.getInputStream();
            BufferedImage originalImage = ImageIO.read(inputStream);

            Thumbnails.of(originalImage)
                    .size(100, 100) // Resize de l'image à 100x100 pixels
                    .outputQuality(0.7) // Réduction de la qualité à 70% de celle de l'originale
                    .toFile(outputFile);

            return "/img/profilePictures/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updatePoints(User updatedUser, int nbr){
        // Call la fonction add et save dans la bdd
        updatedUser.addPoints(nbr);
        userRepository.save(updatedUser);
    }
}
