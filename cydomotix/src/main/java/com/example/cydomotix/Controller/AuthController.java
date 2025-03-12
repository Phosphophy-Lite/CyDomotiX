package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.User;
import com.example.cydomotix.Service.UserService;
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

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {

        // Check if username already exists in the database
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "auth/login";  // If there are errors, show the form again
        }

        userService.registerUser(user); // Encrypt password and save to DB

        return "redirect:/login?registerSuccess=true"; // Redirect to the login page after successful registration
    }

    @GetMapping("/login")  // Ensure a GET mapping for login page
    public String showLoginForm(Model model) {

        // Vérifie si l'utilisateur a déjà une session authentifiée en cours
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) { // anonymousUser : pas loggé
            return "redirect:/dashboard"; // Rediriger les utilisateurs connectés au dashboard
        }

        model.addAttribute("user", new User()); // Nécessaire pour le formulaire d'inscription caché
        return "auth/login";  // Updated path
    }


}
