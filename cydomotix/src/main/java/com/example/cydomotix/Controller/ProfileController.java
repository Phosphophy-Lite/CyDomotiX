package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewProfile(Model model) {
        // Récupère la session authentifiée en cours
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            // Récupérer le pseudo de l'utilisateur authentifié, dans le contexte du User de Spring Security (qui ne stocke que mdp + login)
            String username = authentication.getName();

            // Récupérer l'entité complète User de la BDD
            Optional<User> userOptional = userService.getByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);
            }
        } else {
            return "redirect:/error";
        }

        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                @RequestParam(value = "oldPswd", required = false) String oldPswd,
                                @RequestParam(value = "pfp", required = false) MultipartFile newProfilePicture,
                                RedirectAttributes redirectAttributes) {
        // Récupère la session authentifiée en cours
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            // Récupérer le pseudo de l'utilisateur authentifié, dans le contexte du User de Spring Security (qui ne stocke que mdp + login)
            String username = authentication.getName();

            // Récupérer l'entité complète User de la BDD
            Optional<User> userOptional = userService.getByUsername(username);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();

                // S'assurer que le pseudo (unique) renseigné dans le form n'est pas déjà pris si il est modifié, sauf si il reste inchangé
                boolean nameExists = userService.usernameExists(updatedUser.getUsername());
                if (nameExists && !existingUser.getUsername().equals(updatedUser.getUsername())) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Ce pseudonyme est déjà utilisé.");
                    return "redirect:/profile";
                }

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String newPswd = updatedUser.getPassword(); // The new password input

                if(newPswd != null && !newPswd.isEmpty()) {
                    if(oldPswd == null || !passwordEncoder.matches(oldPswd, existingUser.getPassword())) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Le mot de passe saisi ne correspond pas à l'ancien mot de passe.");
                        return "redirect:/profile";
                    }
                    if(newPswd.length() < 6){
                        redirectAttributes.addFlashAttribute("errorMessage", "Le nouveau mot de passe doit faire 6 caractères minimum.");
                        return "redirect:/profile";
                    }
                    existingUser.setPassword(passwordEncoder.encode(newPswd));
                }

                // Sauvegarder User et ses attributs en BDD
                userService.update(authentication, existingUser.getId(), updatedUser);

                if(newProfilePicture != null){
                    // Vérifie la taille du fichier uploadé si il n'est pas trop gros
                    if (newProfilePicture.getSize() > 10485760) { // 10MB en bytes
                        redirectAttributes.addFlashAttribute("errorMessage", "La taille du fichier est trop grande.");
                        return "redirect:/profile";
                    }

                    // Gérer l'upload de l'image
                    if (!newProfilePicture.isEmpty()) {
                        String imageName = userService.saveProfilePicture(newProfilePicture, existingUser.getId());
                        userService.setUserProfilePicture(existingUser,imageName); // Stocker le nom du fichier dans la BDD
                    }
                }
            }
        } else {
            return "redirect:/error";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Informations modifiées avec succès.");
        return "redirect:/profile";
    }

    @GetMapping("/{username}")
    public String viewPublicUserProfile(@PathVariable String username, Model model) {
        // Récupérer l'entité complète User de la BDD
        Optional<User> userOptional = userService.getByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        }
        else{
            return "redirect:/error";
        }

        return "public-profile";
    }
}
