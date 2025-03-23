package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.AttributeValue;
import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Repository.Objects.AttributeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeValueService {

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private ConnectedObjectService connectedObjectService;

    /**
     * Sauvegarde la valeur d'un attribut dans la BDD.
     * @param attributeValue
     */
    public void save(AttributeValue attributeValue) {
        attributeValueRepository.save(attributeValue);
        System.out.println("attribute value saved");
    }

    /**
     * Supprime toutes les valeurs d'attribut d'un objet connecté spécifié par son id
     * @param id Id de l'object connecté (Connected Object)
     */
    public void deleteByConnectedObjectId(Integer id){
        ConnectedObject object = connectedObjectService.getConnectedObjectById(id);
        List<AttributeValue> attValues = object.getAttributeValueList();
        attributeValueRepository.deleteAll(attValues);
    }

}
