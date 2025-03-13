package com.example.cydomotix.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="`User`") // indique le nom de cette table associée
public class User implements UserDetails {

    @Id // indique que cet attribut est la primary key de la table
    @GeneratedValue(strategy= GenerationType.IDENTITY) // id_user doit être auto incrémenté
    private Integer id_user;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Un pseudonyme est obligatoire.")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Un mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caractères.")
    private String password;

    @Column(nullable = false)
    private String access_type = "USER"; // par défaut

    private int age;
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true) // Allow null values
    private LocalDate birth_date;

    private String member_type;
    private String photo;
    private String first_name;
    private String last_name;
    private String experience_level;
    private int points;

    private boolean is_connected;


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

    public Integer getId(){
        return this.id_user;
    }

    public int getAge(){
        return this.age;
    }
    public String getGender(){
        return this.gender;
    }
    public LocalDate getBirthDate(){
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

    public void setUsername(String newUsername){
        this.username = newUsername;
    }
    public void setAge(int newAge){
        this.age = newAge;
    }
    public void setGender(String newGender){
        this.gender = newGender;
    }
    public void setBirthDate(LocalDate newBirthDate){
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

}

