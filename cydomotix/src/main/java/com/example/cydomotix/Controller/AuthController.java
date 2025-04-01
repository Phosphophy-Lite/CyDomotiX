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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public String registerUser(@Valid User user, BindingResult bindingResult, @RequestParam("photo") MultipartFile profilePicture, RedirectAttributes redirectAttributes) {
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
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("registrationError", "Veuillez corriger les erreurs dans le formulaire.");
            return "redirect:/login?show=register"; // Redirection vers le formulaire avec messages stockés temporairement
        }

        System.out.println("Avant enregistrement");
        // Enregistrer l'utilisateur dans la BDD (avec encryption de mot de passe) et récupère ses infos dans un objet
        User registeredUser = userService.registerUser(user);

        // Gérer l'upload de l'image
        System.out.println("Avant le if ntm");
        if (!profilePicture.isEmpty()) {
            System.out.println("Coucou je suis dans le if");
            String imageName = saveProfilePicture(profilePicture, registeredUser.getId());
            userService.setUserProfilePicture(registeredUser,imageName); // Stocker le nom du fichier dans la BDD
        }

        // Nous ne voulons pas stocker une image avant d'enregistrer l'utilisateur,
        // sinon nous stockerions des images inutiles en cas d'erreur lors de l'enregistrement de l'utilisateur.

        // Nous avons besoin d'un moyen pour récupérer l'image envoyée dans le formulaire,
        // et obtenir un String uniqueFileName si l'enregistrement de l'image réussit également.

        // Mettre à jour la photo de profil de l'utilisateur enregistré
        // setUserProfilePicture(registeredUser, uniqueFileName);

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
                System.out.println("Erreur : Aucun fichier reçu !");
                return null;
            }
            // Créer un répertoire si inexistant
            String uploadDir = "src/main/resources/static/img/profilePictures/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("Crated new directory: " + uploadDir);
            }
            // Vérifier et récupérer l'extension du fichier
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                System.out.println("Erreur : Impossible de déterminer l'extension du fichier !");
                return null;
            }

            // Générer un nom de fichier unique
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String uniqueFileName = "profile_" + userId + fileExtension;
            File outputFile = new File(uploadDir + uniqueFileName);
            System.out.println("Test 1");

            // Compression de l'image avec Thumbnailator
            InputStream inputStream = file.getInputStream();
            BufferedImage originalImage = ImageIO.read(inputStream);

            Thumbnails.of(originalImage)
                    .size(100, 100) // Resize de l'image à 100x100 pixels
                    .outputQuality(0.7) // Réduction de la qualité à 70% de celle de l'originale
                    .toFile(outputFile);
            System.out.println("Resize de l'image");

            return uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
