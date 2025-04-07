package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Envoie un mail au serveur SMTP défini dans application.properties via JavaMailSender
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Contenu du mail
     * @param user - l'utilisateur dont il faut récupérer l'adresse email renseignée
     * @param token - le token associé, généré à la création de son compte
     */
    public void sendVerificationEmail(User user, String token) {
        String subject = "Vérification de votre compte CyDomotiX";
        String confirmationUrl = "http://localhost:8080/verify?token=" + token;
        String message = "Cliquez sur le lien suivant pour activer votre compte : " + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
