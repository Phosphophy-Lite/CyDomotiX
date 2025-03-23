package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ObjectType")
public class ObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_object_type")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    // Une seule instance d'ObjectType peut avoir plusieurs ObjectAttribute associés
    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectAttribute> attributes = new ArrayList<>();

    // Une seule instance d'ObjectType peut avoir plusieurs ConnectedObject associés
    @OneToMany(mappedBy = "objectType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConnectedObject> objects = new ArrayList<>();  // Liste des objets de ce type

    public Integer getId() {
        return id;
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
