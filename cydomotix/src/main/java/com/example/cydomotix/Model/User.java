package com.example.cydomotix.Model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="`User`") // indique le nom de cette table associée
public class User implements UserDetails {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_user doit être auto incrémenté
    private Long id_user;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String access_type; // "ROLE_USER", "ROLE_ADMIN" etc

    private int age;
    private String gender;
    private Date birth_date;
    private String member_type;
    private String photo;
    private String first_name;
    private String last_name;
    private String experience_level;
    private int points;

    private boolean is_connected;
    private int id_house; // Non-null foreign key


    public User(String username, String password, String access_type) {
        this.username = username;
        this.password = password;
        this.access_type = access_type;
    }

    public User() {

    }

    // Security methods for Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_" + access_type);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

