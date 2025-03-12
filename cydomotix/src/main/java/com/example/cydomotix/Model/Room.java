package com.example.cydomotix.Model;

import jakarta.persistence.*;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="Room") // indique le nom de cette table associée
public class Room {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_house doit être auto incrémenté
    private long id_room;

    private String name;
    private String type;
    private long id_house; // Clé étangère

    public long getId_room(){
        return this.id_room;
    }
    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public long getId_house(){
        return this.id_room;
    }

    public void setId_room(long NewId_room){
        this.id_room = NewId_room;
    }
    public void setName(String NewName){
        this.name = NewName;
    }
    public void setType(String NewType){
        this.type = NewType;
    }
    public void setId_house(long NewId_house){
        this.id_house = NewId_house;
    }

}
