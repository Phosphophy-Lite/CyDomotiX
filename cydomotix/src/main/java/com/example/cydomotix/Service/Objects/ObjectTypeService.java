package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Model.Users.ActionType;
import com.example.cydomotix.Repository.Objects.ObjectTypeRepository;
import com.example.cydomotix.Service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectTypeService {

    @Autowired
    private ObjectTypeRepository objectTypeRepository;

    @Autowired
    private UserActionService userActionService;

    /**
     * Vérifie si un nom de type d'objet donné n'est pas déjà existant dans la BDD
     * @param name Nom du Type d'objet
     * @return 1 : le type figure dans la BDD / 0 : le type n'existe pas dans la BDD
     */
    public boolean objectTypeNameExists(String name) {
        return objectTypeRepository.findByName(name).isPresent();
    }

    /**
     * Sauvegarde un nouveau Type d'objet connecté dans la BDD
     * @param objectType Le Type d'objet à sauvegarder
     */
    public void save(ObjectType objectType, String username) {

        // Vérifie si le Type d'objet (utilisant ce nom) existe déjà
        if (objectTypeNameExists(objectType.getName())) {
            throw new IllegalArgumentException("Object Type name already exists.");
        }

        // Sauvegarde automatiquement les listes des attributs aussi grâce à CascadeType.ALL spécifié dans l'attribut de classe
        objectTypeRepository.save(objectType);
        userActionService.logAction(username, ActionType.ADD_TYPE, objectType.getName()); // logger l'action utilisateur
    }

    /**
     * Récupère tous les Types d'objet
     * @return
     */
    public List<ObjectType> getAllObjectTypes() {
        return objectTypeRepository.findAll();
    }

    /**
     * Récupère le type d'objet de l'id spécifié
     * @param id
     * @return
     */
    public ObjectType getObjectTypeById(Integer id) {
        if(objectTypeRepository.findById(id).isPresent()) {
            return objectTypeRepository.findById(id).get();
        }
        else {
            throw new IllegalArgumentException("Object Type with id " + id + " does not exist.");
        }
    }

    /**
     * Supprime un Type d'objet donné de la BDD
     * @param id : clé primaire id
     */
    public void deleteObjectType(final Integer id, String username) {
        ObjectType type = objectTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Object Type with id " + id + " does not exist."));
        userActionService.logAction(username, ActionType.DELETE_TYPE, type.getName()); // logger l'action utilisateur
        objectTypeRepository.deleteById(id);
    }

}
