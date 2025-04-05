package com.example.cydomotix.Controller;

import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {
    /*
        Controller gérant les requêtes HTTP
        => Méthodes servent à retourner des vues HTML (nom du fichier HTML)
     */

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        // Vérifie si l'utilisateur a déjà une session authentifiée en cours
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) { // anonymousUser : pas loggé
            return "redirect:/dashboard"; // Rediriger les utilisateurs connectés au dashboard
        }

        // Pas loggé : charge la page home.html quand on accède à la racine "/" du serveur web
        return "home";
    }

    @GetMapping("dev/page2")
    public String showPage2(){
        return "dev/page2";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
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

                // Récupérer la liste des autres utilisateurs
                List<User> otherUsers = new ArrayList<>(userService.getAllUsers());
                otherUsers.remove(user); // Retirer l'utilisateur actuel de la liste

                model.addAttribute("userList", otherUsers);
            }
        } else { // Session cassée, rediriger vers erreur
            return "redirect:/error";
        }

        return "dashboard";
    }
}
