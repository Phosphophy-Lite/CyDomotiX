package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ObjectType")
public class ObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_object_type;

    @Column(nullable = false, unique = true)
    private String name;

    // Can be associated to multiple instances of ObjectAttributes
    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectAttribute> attributes = new ArrayList<>();

    // Can be associated to multiple instances of ConnectedObjects
    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConnectedObject> objects;  // Liste des objets de ce type

    public Integer getId() {
        return id_object_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ObjectAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<ConnectedObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ConnectedObject> objects) {
        this.objects = objects;
    }
}
