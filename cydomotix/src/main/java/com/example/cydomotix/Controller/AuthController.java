package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.User;
import com.example.cydomotix.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";  // Returns the registration form
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        // Check if username already exists in the database
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "register";  // If there are errors, show the form again
        }

        userService.registerUser(user); // Encrypt the password before saving

        return "redirect:/login"; // Redirect to the login page after successful registration
    }
}
