package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "AttributeValue")
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_attribute_value")
    private Integer id;

    private Integer int_value;
    private Double double_value;
    private String string_value;

    // Plusieurs instances d'AttributeValue peuvent être associées à 1 instance des entités ci dessous

    // Clé étrangère pointant vers entité mère (ConnectedObject)
    @ManyToOne
    @JoinColumn(name = "connected_object_id", nullable = false)
    private ConnectedObject connectedObject;

    // Clé étrangère pointant vers entité mère (ObjectAttribute)
    @ManyToOne
    @JoinColumn(name = "object_attribute_id", nullable = false)
    private ObjectAttribute objectAttribute;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
