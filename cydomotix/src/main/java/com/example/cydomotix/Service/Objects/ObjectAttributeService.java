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

    /**
     * Save an attribute (for an object type)
     * @param objectAttribute
     */
    public void save(ObjectAttribute objectAttribute) {
        objectAttributeRepository.save(objectAttribute);
        System.out.println("object attribute saved");
    }

    /**
     * Save multiple attributes for a specified type of object
     * @param objType The object type to save
     * @param attributeNames The names of the attributes
     * @param attributeTypeValues The types of the attributes (INT, BOOLEAN...)
     */
    public void saveAttributesForObjectType(ObjectType objType, List<String> attributeNames, List<String> attributeTypeValues) {
        for (int i = 0; i < attributeNames.size(); i++) {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setName(attributeNames.get(i));
            objectAttribute.setValueType(attributeTypeValues.get(i));
            objectAttribute.setObjectType(objType);  // Associate attribute to object type
            save(objectAttribute);
        }
    }

    // Récupérer tous les attributs d'un type d'objet
    public List<ObjectAttribute> getAttributesForObjectType(ObjectType objType) {
        return objectAttributeRepository.findByObjectType(objType);
    }

}
