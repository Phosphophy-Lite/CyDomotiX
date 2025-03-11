package com.example.cydomotix.Model;

import jakarta.persistence.*;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="Maison") //indique le nom de cette table associée
public class Maison {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_Maison doit être auto incrémenté
    private Integer id_Maison;

    private String Nom;

    public Integer getId_Maison(){
        return this.id_Maison;
    }
    public String getNom(){
        return this.Nom;
    }

    public void setId_Maison(Integer newId){
        this.id_Maison = newId;
    }
    public void setNom(String newNom){
        this.Nom = newNom;
    }
}