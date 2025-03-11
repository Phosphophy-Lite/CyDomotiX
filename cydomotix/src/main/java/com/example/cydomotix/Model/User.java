package com.example.cydomotix.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.sql.Date;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="User") // indique le nom de cette table associée
public class User {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_user doit être auto incrémenté
    private Integer id_user;

    @NotBlank(message = "Username is required")
    private String username;
    private int age;
    private String gender;
    private Date birth_date;
    private String member_type;
    private String photo;
    private String first_name;
    private String last_name;
    private String access_type;
    private String experience_level;
    private int points;

    @NotBlank(message = "Password is required")
    private String password;

    private boolean is_connected;
    private int id_house; // Non-null foreign key

    public String getUsername() {
        return this.username;
    }
    public int getAge(){
        return this.age;
    }
    public String getGender(){
        return this.gender;
    }
    public Date getBirthDate(){
        return this.birth_date;
    }
    public String getMemberType(){
        return this.member_type;
    }
    public String getPhoto(){
        return this.photo;
    }
    public String getFirstName(){
        return this.first_name;
    }
    public String getLastName(){
        return this.last_name;
    }
    public String getAccessType(){
        return this.access_type;
    }
    public String getExperienceLevel(){
        return this.experience_level;
    }
    public int getPoints(){
        return this.points;
    }
    public String getPassword(){
        return this.password;
    }
    public boolean isConnected(){
        return this.is_connected;
    }
    public int getIdHouse(){
        return this.id_house;
    }

    public void setUsername(String newUsername){
        this.username = newUsername;
    }
    public void setAge(int newAge){
        this.age = newAge;
    }
    public void setGender(String newGender){
        this.gender = newGender;
    }
    public void setBirthDate(Date newBirthDate){
        this.birth_date = newBirthDate;
    }
    public void setMemberType(String newMemberType){
        this.member_type = newMemberType;
    }
    public void setPhoto(String newPhoto){
        this.photo = newPhoto;
    }
    public void setFirstName(String newFirstName){
        this.first_name = newFirstName;
    }
    public void setLastName(String newLastName){
        this.last_name = newLastName;
    }
    public void setAccessType(String newAccessType){
        this.access_type = newAccessType;
    }
    public void setExperienceLevel(String newExperienceLevel){
        this.experience_level = newExperienceLevel;
    }
    public void setPoints(int newPoints){
        this.points = newPoints;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setConnected(boolean newConnected){
        this.is_connected = newConnected;
    }
    public void setIdHouse(int newIdHouse){
        this.id_house = newIdHouse;
    }

}

