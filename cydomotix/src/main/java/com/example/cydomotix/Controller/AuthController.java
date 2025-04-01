package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.User;
import com.example.cydomotix.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Réception d'une requête POST du formulaire register sur la page de login pour l'inscription
     * @param user
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam("pfp") MultipartFile profilePicture, RedirectAttributes redirectAttributes) {
        /* @Valid garantit que l'objet User reçu via le formulaire respecte les contraintes :
           @NotNull, @Size, etc., définies sur ses attributs dans la classe User.

           BindingResult contient les erreurs de validation si l'objet ne satisfait pas ces contraintes.

           User stocke l'objet utilisateur temporaire contenant les attributs remplis lors de l'inscription.
        */


        // Vérifie si le nom d'utilisateur existe déjà dans la BDD, si oui, renvoyer un message d'erreur à la vue de l'utilisateur
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Le nom d'utilisateur existe déjà.");
        }

        // Autorise à ne pas remplir forcément le champ date de naissance
        // Si le champ du formulaire est laissé vide par l'utilisateur, le formulaire va envoyer un String vide
        // C'est un problème car on autorise les valeurs null, mais pas les String non null mais vide car il y aura un problème de format de Date non reconnu
        if (user.getBirthDate() != null && user.getBirthDate().toString().isEmpty()) {
            user.setBirthDate(null); // Remplir alors cet attribut par null si le String envoyé est vide
        }

        // Afficher tous les messages d'erreur à la vue utilisateur
        if (bindingResult.hasErrors()) {
            System.out.println("Form has errors:");
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("registrationError", "Veuillez corriger les erreurs dans le formulaire.");
            return "redirect:/login?show=register"; // Redirection vers le formulaire avec messages stockés temporairement
        }

        // Enregistrer l'utilisateur dans la BDD (avec encryption de mot de passe) et récupère ses infos dans un objet
        User registeredUser = userService.registerUser(user);

        // Gérer l'upload de l'image
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String imageName = saveProfilePicture(profilePicture, registeredUser.getId());
            userService.setUserProfilePicture(registeredUser,imageName); // Stocker le nom du fichier dans la BDD
        }

        System.out.println("Successfully added " + user.getUsername() + "to the database.");

        return "redirect:/login?registerSuccess=true"; // Redirect to the login page after successful registration
    }

    /**
     * Affichage de la page login (requête GET)
     * Redirection vers dashboard si l'utilisateur est déjà authentifié
     * @param session
     * @param model
     * @return auth/login : la page html de login
     */
    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {

        // Vérifie si l'utilisateur a déjà une session authentifiée en cours
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) { // anonymousUser : pas loggé
            return "redirect:/dashboard"; // Rediriger les utilisateurs connectés au dashboard
        }

        // Récupère le message d'erreur (login invalides...) de la session (si il y en a un) et l'ajoute au model
        Object errorMessage = session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage); // Afficher le message d'erreur
            session.removeAttribute("errorMessage"); // Le consomme immédiatement et le supprime de la session pour ne pas le réafficher après un rafraichissement de la page
        }

        model.addAttribute("user", new User()); // Nécessaire pour le formulaire d'inscription caché

        return "auth/login";
    }

    private String saveProfilePicture(MultipartFile file, Integer userId) {
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

            return "img/profilePictures/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
