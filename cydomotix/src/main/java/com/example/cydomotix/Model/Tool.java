package com.example.cydomotix.Model;

import jakarta.persistence.*;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="Tool") // indique le nom de cette table associée
public class Tool {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_house doit être auto incrémenté
    private Integer id_tool;

    private String name;
    private Integer id_room; // Clé étrangère

    public Integer getId_tool(){
        return this.id_tool;
    }
    public String getName(){
        return this.name;
    }
    public Integer getId_room(){
        return this.id_room;
    }

    public void setId_tool(Integer NewId_tool){
        id_tool = NewId_tool;
    }
    public void setName(String NewName){
        name = NewName;
    }
    public void setId_room(Integer NewId_room){
        id_room = NewId_room;
    }

}
