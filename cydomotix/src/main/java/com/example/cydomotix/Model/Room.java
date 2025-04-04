package com.example.cydomotix.Model;

import jakarta.persistence.*;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="Room") // indique le nom de cette table associée
public class Room {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_house doit être auto incrémenté
    @Column(name="id_room")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    public Integer getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public void setName(String NewName){
        this.name = NewName;
    }

}
