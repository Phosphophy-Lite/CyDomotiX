package com.example.cydomotix.Service.Objects;

import com.example.cydomotix.Model.Objects.AttributeValue;
import com.example.cydomotix.Repository.Objects.AttributeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeValueService {

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    /**
     * Save the value of an attribute for a specific object
     * @param attributeValue
     */
    public void save(AttributeValue attributeValue) {
        attributeValueRepository.save(attributeValue);
        System.out.println("attribute value saved");
    }

}
