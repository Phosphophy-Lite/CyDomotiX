package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="ConnectedObject") // indique le nom de cette table associée
public class ConnectedObject {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_house doit être auto incrémenté
    @Column(name="id_object")
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String brand;
    private boolean is_active;
    private String mode;
    private String connectivity;
    private LocalDateTime last_interaction;
    private int battery_status;

    private Integer id_room; // Clé étrangère

    // Plusieurs instances de ConnectedObject peuvent être associées à 1 instance d'ObjectType
    // Clé étrangère pointant vers entité mère (ObjectType)
    @ManyToOne
    @JoinColumn(name = "id_type", nullable = false)
    private ObjectType objectType;

    // Une seule instance de ConnectedObject peut avoir plusieurs AttributeValue associées
    // mappedBy : propriétaire de la relation
    // cascade : toutes les opérations (persist, merge, remove) sont étendues aux entités associées
    // orphanRemoval : supprimer l'entité enfant si il n'a plus de parent
    @OneToMany(mappedBy = "connectedObject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeValue> attributeValueList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public LocalDateTime getLastInteraction() {
        return last_interaction;
    }

    public void setLastInteraction(LocalDateTime last_interaction) {
        this.last_interaction = last_interaction;
    }

    public int getBatteryStatus() {
        return battery_status;
    }

    public void setBatteryStatus(int battery_status) {
        this.battery_status = battery_status;
    }

    public boolean getIsActive() {
        return is_active;
    }

    public void setIsActive(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdRoom() {
        return id_room;
    }

    public void setIdRoom(Integer id_room) {
        this.id_room = id_room;
    }

    public List<AttributeValue> getAttributeValueList() {
        return attributeValueList;
    }

    public void setAttributeValueList(List<AttributeValue> attributeValueList) {
        this.attributeValueList = attributeValueList;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }
}
