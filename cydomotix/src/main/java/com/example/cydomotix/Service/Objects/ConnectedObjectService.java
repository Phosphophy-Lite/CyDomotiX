package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.*;
import com.example.cydomotix.Model.Users.ActionType;
import com.example.cydomotix.Repository.Objects.AttributeValueRepository;
import com.example.cydomotix.Repository.Objects.ConnectedObjectRepository;
//import com.example.cydomotix.Repository.Objects.UsageEventRepository;
import com.example.cydomotix.Service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConnectedObjectService {

    @Autowired
    private ConnectedObjectRepository connectedObjectRepository;
    @Autowired
    private AttributeValueRepository attributeValueRepository;
    @Autowired
    private UserActionService userActionService;
    /*@Autowired
    private UsageEventRepository usageEventRepository;*/

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
    public void save(ConnectedObject connectedObject, String username) {

        // Vérifie si un objet utilisant ce nom existe déjà
        if (objectExists(connectedObject.getName())) {
            throw new IllegalArgumentException("Connected Object with this name already exists.");
        }

        connectedObjectRepository.save(connectedObject);
        userActionService.logAction(username, ActionType.ADD_OBJECT, connectedObject.getName());
    }

    /**
     * Mettre à jour un objet existant
     * @param id Identifiant de l'objet connecté
     * @param updatedObject Objet mis à jour avec les nouvelles valeurs
     */
    public void update(Integer id, ConnectedObject updatedObject, String username) {
        connectedObjectRepository.findById(id).ifPresent(existingObject -> {
            // Met à jour les champs basiques en évitant les valeurs vides
            updateBasicFields(existingObject, updatedObject);

            // Met à jour les valeurs des attributs dynamiques
            updateAttributeValues(existingObject, updatedObject.getAttributeValueList());

            // Sauvegarde les modifications
            connectedObjectRepository.save(existingObject);
            userActionService.logAction(username, ActionType.UPDATE_OBJECT, existingObject.getName());
        });
    }

    /**
     * Met à jour les champs basiques d'un objet connecté
     */
    private void updateBasicFields(ConnectedObject existingObject, ConnectedObject updatedObject) {
        existingObject.setRoom(updatedObject.getRoom());
        existingObject.setName(getUpdatedValue(updatedObject.getName(), existingObject.getName()));
        existingObject.setBrand(getUpdatedValue(updatedObject.getBrand(), existingObject.getBrand()));
        existingObject.setMode(updatedObject.getMode());
        existingObject.setConnectivity(updatedObject.getConnectivity());
        existingObject.setBatteryStatus(updatedObject.getBatteryStatus());
        existingObject.setPower( updatedObject.getPower());
        existingObject.setLastInteraction(LocalDateTime.now());
    }

    /**
     * Met à jour les valeurs des attributs dynamiques
     */
    private void updateAttributeValues(ConnectedObject existingObject, List<AttributeValue> updatedValues) {
        Map<Integer, AttributeValue> existingValuesMap = existingObject.getAttributeValueList()
                .stream().collect(Collectors.toMap(av -> av.getObjectAttribute().getId(), av -> av));

        for (AttributeValue updatedValue : updatedValues) {
            AttributeValue existingValue = existingValuesMap.get(updatedValue.getObjectAttribute().getId());

            if (existingValue != null) {
                existingValue.setInt_value(getUpdatedValue(updatedValue.getInt_value(), existingValue.getInt_value()));
                existingValue.setDouble_value(getUpdatedValue(updatedValue.getDouble_value(), existingValue.getDouble_value()));
                if (existingValue.getString_value() != null) { // s'assurer qu'il n'est pas null avant d'appeler trim()
                    existingValue.setString_value(getUpdatedValue(updatedValue.getString_value(), existingValue.getString_value()));
                }
                attributeValueRepository.save(existingValue);
            }
        }
    }

    /**
     * Retourne une valeur mise à jour, ou conserve l'ancienne si la nouvelle est vide
     */
    private String getUpdatedValue(String newValue, String oldValue) {
        return (newValue != null && !newValue.trim().isEmpty()) ? newValue : oldValue;
    }

    private Integer getUpdatedValue(Integer newValue, Integer oldValue) {
        return (newValue != null ? newValue : oldValue);
    }

    private Double getUpdatedValue(Double newValue, Double oldValue) {
        return (newValue != null ? newValue : oldValue);
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
    public void deleteConnectedObject(final Integer id, String username) {
        ConnectedObject object = connectedObjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connected object with id " + id + " does not exist."));
        userActionService.logAction(username, ActionType.DELETE_OBJECT, object.getName());
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
        ConnectedObject connectedObject = connectedObjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connected object with id " + id + " does not exist."));
        return connectedObject;
    }

    public void switchStatus(Integer id, String username) {
        // Vérifie si l'objet connecté existe dans la base de données
        ConnectedObject connectedObject = connectedObjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connected object with id " + id + " does not exist."));

        boolean currentStatus = connectedObject.getIsActive();

        // Met à jour le champ status par son inverse
        connectedObject.setIsActive(!currentStatus);

        /*
        UsageEvent usageEvent = new UsageEvent();
        usageEvent.setConnectedObject(connectedObject);
        usageEvent.setStatus(!currentStatus);*/

        LocalDateTime currentDateTime = LocalDateTime.now();
        connectedObject.setLastInteraction(currentDateTime);

        /*usageEvent.setTimestamp(currentDateTime);
        usageEventRepository.save(usageEvent);*/

        // Sauvegarde l'objet mis à jour dans la BDD
        connectedObjectRepository.save(connectedObject);

        if(!currentStatus){
            userActionService.logAction(username, ActionType.ON_OBJECT, connectedObject.getName());
        } else{
            userActionService.logAction(username, ActionType.OFF_OBJECT, connectedObject.getName());
        }
    }

    public List<ConnectedObject> searchObjects(String keyword, Integer objectTypeId, Integer roomId, String brand, Mode mode, Connectivity connectivity) {
        return connectedObjectRepository.searchObjects(keyword, objectTypeId, roomId, brand, mode, connectivity);
    }

}


