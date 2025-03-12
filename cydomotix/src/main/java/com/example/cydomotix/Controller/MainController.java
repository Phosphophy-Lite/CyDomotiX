package com.example.cydomotix.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    /*
        Controller gérant les requêtes HTTP
        => Méthodes servent à retourner des vues HTML (nom du fichier HTML)
     */

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

    @GetMapping("/page2")
    public String showPage2(){
        return "page2";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

}
