package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Users.ActionType;
import com.example.cydomotix.Model.Users.UserAction;
import com.example.cydomotix.Repository.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
