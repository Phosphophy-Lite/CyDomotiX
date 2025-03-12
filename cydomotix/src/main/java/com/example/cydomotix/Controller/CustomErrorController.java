package com.example.cydomotix.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";  // Corresponds to the "access-denied.html" template
    }

    @RequestMapping("/error")
    public String error() {
        return "error";  // Corresponds to the "access-denied.html" template
    }

}
