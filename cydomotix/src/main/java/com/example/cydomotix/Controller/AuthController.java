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
        /* @Valid ensures the User object received through the form respects @NotNull / @Size, etc, constraints
            on its attributes as defined in the User class
            BindingResult contains validation errors if the object fails this validation.
         */


        // Check if username already exists in the database
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Le nom d'utilisateur existe déjà.");
        }

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("user", user); // Keep user data for form re-population
            model.addAttribute("registrationError", "Veuillez corriger les erreurs dans le formulaire.");
            return "auth/login";  // If there are errors, show the form again
        }

        userService.registerUser(user); // Encrypt password and save to DB
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
