package com.example.cydomotix.Service.Objects;


import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Repository.Objects.ConnectedObjectRepository;
import com.example.cydomotix.Repository.Objects.ObjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectedObjectService {

    @Autowired
    private ConnectedObjectRepository connectedObjectRepository;

    @Autowired
    private ObjectTypeRepository objectTypeRepository;


    /**
     * Vérifie si un objet connecté au nom donné n'est pas déjà existant dans la BDD
     * @param name Nom de l'objet (ex : Thermostat Samsung Salon)
     * @return 1 : l'objet figure dans la BDD / 0 : l'objet n'existe pas dans la BDD
     */
    public boolean objectExists(String name) {
        return connectedObjectRepository.findByName(name).isPresent();
    }

    /**
     * Sauvegarde un nouvel objet connecté dans la BDD
     * @param connectedObject L'objet à sauvegarder
     */
    public void save(ConnectedObject connectedObject) {

        // Vérifie si un objet utilisant ce nom existe déjà
        if (objectExists(connectedObject.getName())) {
            throw new IllegalArgumentException("Connected Object with this name already exists.");
        }

        connectedObjectRepository.save(connectedObject);
    }

    /**
     * Récupère tous les objets connectés
     * @return
     */
    public List<ConnectedObject> getAllConnectedObjects() {
        return connectedObjectRepository.findAll();
    }

    /**
     * Supprime un object connecté donné de la BDD
     * @param id : clé primaire id
     */
    public void deleteConnectedObject(final Integer id) {
        connectedObjectRepository.deleteById(id);
    }

}


