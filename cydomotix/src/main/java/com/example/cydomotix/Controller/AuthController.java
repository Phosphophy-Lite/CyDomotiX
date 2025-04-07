package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Model.Users.VerificationToken;
import com.example.cydomotix.Repository.UserRepository;
import com.example.cydomotix.Repository.VerificationTokenRepository;
import com.example.cydomotix.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Réception d'une requête POST du formulaire register sur la page de login pour l'inscription
     * @param user Stocke l'objet utilisateur temporaire contenant les attributs envoyés par le formulaire
     * @param bindingResult Erreurs de validation si l'objet ne satisfait pas les contraintes
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam("pfp") MultipartFile profilePicture, RedirectAttributes redirectAttributes) {
        /* @Valid garantit que l'objet User reçu via le formulaire respecte les contraintes :
           @NotNull, @Size, etc., définies sur ses attributs dans la classe User.
        */

        // Vérifie si le nom d'utilisateur existe déjà dans la BDD, si oui, renvoyer un message d'erreur à la vue de l'utilisateur
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Le nom d'utilisateur existe déjà.");
        }

        // Vérifie si le mail existe déjà dans la BDD, si oui, renvoyer un message d'erreur à la vue de l'utilisateur
        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "L'adresse email est déjà utilisée.");
        }

        /* Autorise à ne pas remplir forcément le champ date de naissance
         Si le champ du formulaire est laissé vide par l'utilisateur, le formulaire va envoyer un String vide
         C'est un problème car on autorise les valeurs null, mais pas les String non null mais vide car il y aura un problème de format de Date non reconnu */
        if (user.getBirthDate() != null && user.getBirthDate().toString().isEmpty()) {
            user.setBirthDate(null); // Remplir alors cet attribut par null si le String envoyé est vide
        }

        // Vérifie la taille du fichier uploadé si il n'est pas trop gros
        if (profilePicture != null && profilePicture.getSize() > 10485760) { // 10MB en bytes
            bindingResult.rejectValue("photo", "error.user", "La taille du fichier est trop grande.");
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

        if (profilePicture != null) {
            // Gérer l'upload de l'image
            if (!profilePicture.isEmpty()) {
                String imageName = userService.saveProfilePicture(profilePicture, registeredUser.getId());
                userService.setUserProfilePicture(registeredUser,imageName); // Stocker le nom du fichier dans la BDD
            }
        }

        System.out.println("Successfully added " + user.getUsername() + " to the database.");

        return "redirect:/login?registerSuccess=true&show=register"; // Redirect to the login page after successful registration
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

        // Ne pas écraser les données si elles existent déjà (erreur dans le formulaire d'inscription déjà rempli, venant du RedirectAttributes)
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User()); // pour le formulaire d'inscription caché
        }

        return "auth/login";
    }

    /**
     * Requête envoyée quand l'utilisateur clique sur le lien reçu par mail pour vérifier son compte
     * @param token - Token généré et envoyé dans le lien
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        Optional<VerificationToken> tokenOptional = verificationTokenRepository.findByToken(token);

        // token trouvé en BDD
        if (tokenOptional.isPresent()) {
            VerificationToken verificationToken = tokenOptional.get();

            // Date d'expiration du token est antérieure à la date actuelle (token a expiré après ses 24h de validité)
            if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le lien de vérification a expiré.");
                return "redirect:/login";
            }

            User user = verificationToken.getUser(); // Récupérer l'utilisateur associé à ce token unique
            user.setEnabled(true); // Activer son compte
            userRepository.save(user); // Enregistrer cette activation dans la BDD
            verificationTokenRepository.delete(verificationToken); // Supprimer le token après activation

            redirectAttributes.addFlashAttribute("successMessage", "Votre compte a été vérifié avec succès !");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "Lien de vérification invalide.");
        }
        return "redirect:/login";
    }

}
