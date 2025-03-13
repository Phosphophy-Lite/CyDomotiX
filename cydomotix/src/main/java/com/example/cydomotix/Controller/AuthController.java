package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.User;
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

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Reception d'une requête POST du formulaire register sur la page de login pour l'inscription
     * @param user
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        /* @Valid garantit que l'objet User reçu via le formulaire respecte les contraintes :
           @NotNull, @Size, etc., définies sur ses attributs dans la classe User.

           BindingResult contient les erreurs de validation si l'objet ne satisfait pas ces contraintes.

           User stocke l'objet utilisateur temporaire contenant les attributs remplis lors de l'inscription.
        */


        // Vérifie si le nom d'utilisateur existe déjà dans la BDD, si oui, renvoyer un message d'erreur à la vue de l'utilisateur
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Le nom d'utilisateur existe déjà.");
        }

        // Afficher tous les messages d'erreur à la vue utilisateur
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("user", user); // Keep user data for form re-population
            model.addAttribute("registrationError", "Veuillez corriger les erreurs dans le formulaire.");

            return "redirect:auth/login?show=register"; // If there are errors, show the form again
        }

        // Enregistrer l'utilisateur dans la BDD (avec encryption de mot de passe) et récupère ses infos dans un objet
        User registeredUser = userService.registerUser(user);

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


}
