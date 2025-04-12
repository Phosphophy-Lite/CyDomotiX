package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Users.ActionType;
import com.example.cydomotix.Model.Users.User;
import com.example.cydomotix.Model.Users.UserAction;
import com.example.cydomotix.Repository.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;


@Service
public class UserActionService {

    @Autowired
    private UserActionRepository userActionRepository;

    /**
     * Enregistre une action effectuée par un utilisateur.
     * @param authorUsername le nom d'utilisateur de celui qui a effectué l'action
     * @param actionType le type d'action à enregistrer
     * @param entityName le nom de l'entité liée à l'action, null si l'action n'est pas liée à une autre entitée (autre utilisateur, objet, type d'objet...)
     */
    public void logAction(String authorUsername, ActionType actionType, String entityName) {

        // Créer une nouvelle action utilisateur
        UserAction userAction = new UserAction(authorUsername, actionType);
        if(entityName != null && !entityName.isEmpty()){
            userAction.setRelatedEntity(entityName);
        }

        // Sauvegarder l'action dans la base de données
        userActionRepository.save(userAction);
    }

    public List<UserAction> getAllUserActions(){
        return userActionRepository.findAll();
    }

    /**
     * Calcule le taux de connexion des utilisateurs sur une période donnée
     * @param startDate date de début de la période
     * @param endDate date de fin de la période
     * @return taux de connexion en pourcentage
     */
    public double calculateLoginRateBetween(List<User> totalUsers, ZonedDateTime startDate, ZonedDateTime endDate) {
        // Utilisateurs distincts qui se sont connectés (LOGIN) sur la période (pour ne pas compter quand ils se connectent plusieurs fois dans la période)
        List<String> usernamesLoggedIn = userActionRepository.findDistinctUsernamesByActionTypeAndDateBetween(
                ActionType.LOGIN, startDate, endDate);

        // Total d’utilisateurs inscrits (vérifiés) dans la plateforme
        long nbTotalUsers = totalUsers.size();

        if (nbTotalUsers == 0) return 0.0;

        return (double) usernamesLoggedIn.size() / nbTotalUsers * 100;
    }
}
