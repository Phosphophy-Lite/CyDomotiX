package com.example.cydomotix.Model;

import jakarta.persistence.*;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="House") // indique le nom de cette table associée
public class House {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_house doit être auto incrémenté
    private Integer id_house;

    private String name;

    public Integer getId_house(){
        return this.id_house;
    }
    public String getName(){
        return this.name;
    }

    public void setId_house(Integer newId){
        this.id_house = newId;
    }
    public void setName(String newName){
        this.name = newName;
    }
}