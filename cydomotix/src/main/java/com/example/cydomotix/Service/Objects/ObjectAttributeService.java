package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.ObjectAttribute;
import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Repository.Objects.ObjectAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectAttributeService {

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeService objectTypeService;

    /**
     * Sauvegarder un attribut
     * @param objectAttribute
     */
    public void save(ObjectAttribute objectAttribute) {
        objectAttributeRepository.save(objectAttribute);
        System.out.println("object attribute saved");
    }


    // Récupérer tous les attributs d'un type d'objet
    public List<ObjectAttribute> getAttributesForObjectType(ObjectType objType) {
        return objectAttributeRepository.findByObjectType(objType);
    }

    public ObjectAttribute getObjectAttributeById(Integer id) {
        if(objectAttributeRepository.findById(id).isPresent()) {
            return objectAttributeRepository.findById(id).get();
        }
        else {
            throw new IllegalArgumentException("Object Attribute with id " + id + " does not exist.");
        }
    }

    /**
     * Supprime tous les champs d'un type d'objet spécifié par id
     * @param id Id du type d'objet (ObjectType)
     */
    public void deleteByObjectTypeId(Integer id){
        ObjectType type = objectTypeService.getObjectTypeById(id);
        List<ObjectAttribute> attributes = type.getAttributes();
        objectAttributeRepository.deleteAll(attributes);
    }

}
