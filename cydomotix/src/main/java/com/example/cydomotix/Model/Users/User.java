package com.example.cydomotix.Model.Users;

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

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Un mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caractères.")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private com.example.cydomotix.Model.Users.AccessType access_type = com.example.cydomotix.Model.Users.AccessType.USER; // permissions par défaut après inscription

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true) // Autoriser les valeurs null si l'utilisateur ne veut pas renseigner sa date de naissance
    private LocalDate birth_date;

    @Enumerated(EnumType.STRING)
    private MemberType member_type;

    private int points = 0;

    private String photo = "/img/profilePictures/defaultPfp.png";
    private String first_name;
    private String last_name;
    private String experience_level;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name="approved_by_admin", nullable = false)
    private boolean approvedByAdmin;


    public User(String username, String password, com.example.cydomotix.Model.Users.AccessType access_type) {
        this.username = username;
        this.password = password;
        this.access_type = access_type;
        this.enabled = false;
        this.approvedByAdmin = false;
    }

    public User() {
        this.enabled = false;
        this.approvedByAdmin = false;
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
    public boolean isEnabled() {
        return this.enabled;
    }

    public Integer getId(){
        return this.id_user;
    }

    public int calculateAge(){
        if(birth_date == null){
            return -1;
        }
        return Period.between(birth_date, LocalDate.now()).getYears();
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
    public com.example.cydomotix.Model.Users.AccessType getAccessType(){
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addPoints(int nbr){this.points += nbr;} // Ajoute nbr aux nombres de points de l'utilisateur

    public boolean isApprovedByAdmin() {
        return approvedByAdmin;
    }

    public void setApprovedByAdmin(boolean approvedByAdmin) {
        this.approvedByAdmin = approvedByAdmin;
    }
}

