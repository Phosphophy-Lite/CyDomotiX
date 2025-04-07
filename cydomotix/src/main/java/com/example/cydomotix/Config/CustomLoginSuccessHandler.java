package com.example.cydomotix.Config;

import com.example.cydomotix.Model.Users.ActionType;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Service.UserActionService;
import com.example.cydomotix.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService; // Pour accéder à ton User en BDD

    @Autowired
    private UserActionService userActionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String username = authentication.getName();
        Optional<User> userOpt = userService.getByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Ajoute des points à chaque login
            userService.updatePoints(user,1);
            userActionService.logAction(user.getUsername(), ActionType.LOGIN, null);
        }

        // Redirection normale après connexion
        response.sendRedirect("/dashboard");
    }
}
