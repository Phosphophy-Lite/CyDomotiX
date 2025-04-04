package com.example.cydomotix.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;

@Entity // Annotation qui dit que cette classe correspond à une table de notre BDD sql
@Table(name="Users") // Table SQL associée
public class User implements UserDetails {

    @Id // clé primaire
    @GeneratedValue(strategy= GenerationType.IDENTITY) // automatically incremented
    private Integer id_user;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Un pseudonyme est obligatoire.")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Un mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caractères.")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessType access_type = AccessType.USER; // permissions par défaut après inscription

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true) // Autoriser les valeurs null si l'utilisateur ne veut pas renseigner sa date de naissance
    private LocalDate birth_date;

    @Enumerated(EnumType.STRING)
    private MemberType member_type;

    private int points = 0;

    private String photo = "img/profilePictures/defaultPfp.png";
    private String first_name;
    private String last_name;
    private String experience_level;


    public User(String username, String password, AccessType access_type) {
        this.username = username;
        this.password = password;
        this.access_type = access_type;
    }

    public User() {

    }

    // Méthode de récupération des permissions pour Spring Security
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

    private int calculateAge(){
        LocalDate current_date = LocalDate.now();
        Period period = Period.between(birth_date, current_date);
        return period.getYears();
    }

    public Gender getGender(){
        return this.gender;
    }
    public LocalDate getBirthDate(){
        return this.birth_date;
    }
    public MemberType getMemberType(){
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
    public AccessType getAccessType(){
        return this.access_type;
    }
    public String getExperienceLevel(){
        return this.experience_level;
    }
    public int getPoints(){
        return this.points;
    }

    public void setUsername(String newUsername){
        this.username = newUsername;
    }

    public void setGender(Gender newGender){
        this.gender = newGender;
    }
    public void setBirthDate(LocalDate newBirthDate){
        this.birth_date = newBirthDate;
    }
    public void setMemberType(MemberType newMemberType){
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
    public void setAccessType(AccessType newAccessType){
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

    public void addPoints(int nbr){this.points += nbr;} // Ajoute nbr aux nombres de points de l'utilisateur
}

