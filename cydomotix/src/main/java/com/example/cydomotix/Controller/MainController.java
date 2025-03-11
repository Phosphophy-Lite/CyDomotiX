package com.example.cydomotix.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String home(Model model) {

        /* Charge la page home.html quand on accède à la racine "/" du serveur web */
        return "home";
    }

    @GetMapping("/page2")
    public String showPage2(){
        return "page2";
    }

}
