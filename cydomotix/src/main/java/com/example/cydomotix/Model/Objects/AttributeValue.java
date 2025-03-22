package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "AttributeValue")
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_attribute_value;

    private Integer int_value;
    private Double double_value;
    private String string_value;

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

    public Integer getInt_value() {
        return int_value;
    }

    public void setInt_value(Integer int_value) {
        this.int_value = int_value;
    }

    public Double getDouble_value() {
        return double_value;
    }

    public void setDouble_value(Double double_value) {
        this.double_value = double_value;
    }

    public String getString_value() {
        return string_value;
    }

    public void setString_value(String string_value) {
        this.string_value = string_value;
    }
}
