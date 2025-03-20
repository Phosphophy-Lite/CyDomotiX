package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "ObjectAttribute")
public class ObjectAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_object_attribute;

    @Column(nullable = false)
    private String name;

    private String value_type; // Ex : "INTEGER", "BOOLEAN", "STRING", "DOUBLE"...

    // Many instances of ObjectAttribute can be associated to one instance of ObjectType
    // <=> Foreign key pointing to parent entity
    @ManyToOne
    @JoinColumn(name = "object_type_id", nullable = false)
    private ObjectType objectType;

    public Integer getId() {
        return id_object_attribute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueType() {
        return value_type;
    }

    public void setValueType(String value_type) {
        this.value_type = value_type;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }
}
