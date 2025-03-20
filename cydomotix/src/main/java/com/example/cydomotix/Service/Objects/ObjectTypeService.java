package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Repository.Objects.ObjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectTypeService {

    @Autowired
    private ObjectTypeRepository objectTypeRepository;

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
    public void save(ObjectType objectType) {

        // Vérifie si le Type d'objet (utilisant ce nom) existe déjà
        if (objectTypeNameExists(objectType.getName())) {
            throw new IllegalArgumentException("Object Type name already exists.");
        }

        objectTypeRepository.save(objectType);
    }

    /**
     * Récupère tous les Types d'objet
     * @return
     */
    public List<ObjectType> getAllObjectTypes() {
        return objectTypeRepository.findAll();
    }

    /**
     * Supprime un Type d'objet donné de la BDD
     * @param id : clé primaire id
     */
    public void deleteObjectType(final Integer id) {
        objectTypeRepository.deleteById(id);
    }

}
