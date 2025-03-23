package com.example.cydomotix.Model.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ObjectAttribute")
public class ObjectAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_object_attribute")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ValueType value_type; // Ex : "INTEGER", "STRING", "DOUBLE"...

    // Plusieurs instances d'ObjectAttribute peuvent être associées à 1 instance d'ObjectType
    // Clé étrangère pointant vers entité mère (ObjectType)
    @ManyToOne
    @JoinColumn(name = "object_type_id", nullable = false)
    @JsonIgnore // Ignorer cette propriété lors de la sérialisation en JSON pour les requêtes et ainsi éviter une référence circulaire infinie
                // (nécessaire pour créer un object et sélectionner son type qui envoie une requête GET Json)
    private ObjectType objectType;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValueType getValueType() {
        return value_type;
    }

    public void setValueType(ValueType value_type) {
        this.value_type = value_type;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }
}
