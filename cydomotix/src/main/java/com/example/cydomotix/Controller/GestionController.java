package com.example.cydomotix.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gestion")
public class GestionController {

    @GetMapping
    public String viewGestionDashboard() {
        return "gestion/gestion";
    }
}
