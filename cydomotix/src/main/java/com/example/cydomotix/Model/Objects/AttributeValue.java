package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "AttributeValue")
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_attribute_value;
    private String value;

    // Many instances of AttributeValue can be associated to one instance of these entities

    // <=> Foreign key pointing to parent entity (ConnectedObject)
    @ManyToOne
    @JoinColumn(name = "connected_object_id", nullable = false)
    private ConnectedObject connectedObject;

    // Foreign key pointing to parent ObjectAttribute entity
    @ManyToOne
    @JoinColumn(name = "object_attribute_id", nullable = false)
    private ObjectAttribute objectAttribute;


    public Integer getId() {
        return id_attribute_value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ConnectedObject getConnectedObject() {
        return connectedObject;
    }

    public void setConnectedObject(ConnectedObject connectedObject) {
        this.connectedObject = connectedObject;
    }

    public ObjectAttribute getObjectAttribute() {
        return objectAttribute;
    }

    public void setObjectAttribute(ObjectAttribute objectAttribute) {
        this.objectAttribute = objectAttribute;
    }
}
