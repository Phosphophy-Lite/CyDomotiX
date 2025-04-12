package com.example.cydomotix.Model.Objects;

import com.example.cydomotix.Model.Room;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
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

    @Enumerated(EnumType.STRING)
    private Mode mode;

    @Enumerated(EnumType.STRING)
    private Connectivity connectivity;

    private ZonedDateTime last_interaction;
    private int battery_status;
    private double power;

    // Plusieurs instances de ConnectedObject peuvent être associées à 1 instance de Room
    // Clé étrangère pointant vers entité mère (Room)
    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room; // Clé étrangère

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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Connectivity getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(Connectivity connectivity) {
        this.connectivity = connectivity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ZonedDateTime getLastInteraction() {
        return last_interaction;
    }

    public void setLastInteraction(ZonedDateTime last_interaction) {
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }
}
