package com.example.cydomotix.Model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="Utilisateur") //indique le nom de cette table associée
public class Utilisateur {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_Maison doit être auto incrémenté
    private Integer id_Utilisateur;

    private String Pseudonyme;
    private int Age;
    private String Sexe;
    private Date DateNaissance;
    private String TypeDeMembre;
    private String Photo;
    private String Prenom;
    private String Nom;
    private String TypeAcces;
    private String NiveauExp;
    private int NbPts;
    private String MotDePasse;
    private boolean estConnecte;
    private int id_Maison; //Clé étrangère NON NUL

    public String getPseudonyme() {
        return this.Pseudonyme;
    }
    public int getAge(){
        return this.Age;
    }
    public String getSexe(){
        return this.Sexe;
    }
    public Date getDateNaissance(){
        return this.DateNaissance;
    }
    public String getTypeDeMembre(){
        return this.TypeDeMembre;
    }
    public String getPhoto(){
        return this.Photo;
    }
    public String getPrenom(){
        return this.Prenom;
    }
    public String getNom(){
        return this.Nom;
    }
    public String getTypeAcces(){
        return this.TypeAcces;
    }
    public String getNiveauExp(){
        return this.NiveauExp;
    }
    public int getNbPts(){
        return this.NbPts;
    }
    public String getMotDePasse(){
        return this.MotDePasse;
    }
    public boolean getEstConnecte(){
        return this.estConnecte;
    }
    public int getId_Maison(){
        return this.id_Maison;
    }

    public void setPseudonyme(String newPseudonyme){
        this.Pseudonyme = newPseudonyme;
    }
    public void setAge(int newAge){
        this.Age = newAge;
    }
    public void setSexe(String newSexe){
        this.Sexe = newSexe;
    }
    public void setDateNaissance(Date newDateNaissance){
        this.DateNaissance = newDateNaissance;
    }
    public void setTypeDeMembre(String newTypeDeMembre){
        this.TypeDeMembre = newTypeDeMembre;
    }
    public void setPhoto(String newPhoto){
        this.Photo = newPhoto;
    }
    public void setPrenom(String newPrenom){
        this.Prenom = newPrenom;
    }
    public void setNom(String newNom){
        this.Nom = newNom;
    }
    public void setTypeAcces(String newTypeAcces){
        this.TypeAcces = newTypeAcces;
    }
    public void setNiveauExp(String newNiveauExp){
        this.NiveauExp = newNiveauExp;
    }
    public void setNbPts(int newNbPts){
        this.NbPts = newNbPts;
    }
    public void setMotDePasse(String newMotDePasse){
        this.MotDePasse = newMotDePasse;
    }
    public void setEstConnecte(boolean newEstConnecte){
        this.estConnecte = newEstConnecte;
    }
    public void setId_Maison(int newId_Maison){
        this.id_Maison = newId_Maison;
    }

}
