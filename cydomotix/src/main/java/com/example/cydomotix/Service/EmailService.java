package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Envoie un mail au serveur SMTP défini dans application.properties via JavaMailSender
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.enabled:true}") // true par défaut
    private boolean emailEnabled; // propriété vérification par email activée ou non

    @Autowired
    public EmailService(
            Optional<JavaMailSender> mailSenderOpt
    ) {
        this.mailSender = mailSenderOpt.orElse(null);
        this.emailEnabled = mailSender != null;
    }

    public void sendVerificationEmail(User user, String token) {
        if (!emailEnabled) {
            System.out.println("Email service désactivé : aucun SMTP configuré.");
            return;
        }

        String subject = "Vérification de votre compte CyDomotiX";
        String confirmationUrl = "http://localhost:8080/verify?token=" + token;
        String message = "Cliquez sur le lien suivant pour activer votre compte : " + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    public boolean isEnabled(){
        return emailEnabled;
    }
}

