package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Repository.Objects.ConnectedObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectedObjectService {

    @Autowired
    private ConnectedObjectRepository connectedObjectRepository;

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

    /**
     * Récupère tous les objets connectés d'un type d'objet spécifié par son id
     * @param id Id du type d'objet par lequel filtrer
     * @return Liste de ConnectedObjects du type spécifié
     */
    public List<ConnectedObject> getConnectedObjectsByObjectTypeId(Integer id) {
        return getAllConnectedObjects().stream()
                .filter(obj -> obj.getObjectType().getId().equals(id))
                .collect(Collectors.toList());
    }

    /**
     * Récupère l'objet connecté de l'id spécifié
     * @param id Id du ConnectedObject à récupérer
     * @return ConnectedObject de l'id spécifié
     */
    public ConnectedObject getConnectedObjectById(Integer id) {
        if(connectedObjectRepository.findById(id).isPresent()) {
            return connectedObjectRepository.findById(id).get();
        }
        else {
            throw new IllegalArgumentException("Connected object with id " + id + " does not exist.");
        }
    }


}


