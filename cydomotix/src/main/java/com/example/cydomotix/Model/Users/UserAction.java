package com.example.cydomotix.Model.Users;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="UserAction")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user_action")
    private Integer id;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name="action_type")
    private ActionType actionType;

    /* Stocker l'utilisateur lié à l'action + une autre entité liée à l'action à partir de leur nom
     pour que ces informations soient toujours loggées même si
     l'utilisateur n'existe plus ou l'autre entité liée n'existe plus.
     */
    private String author;

    @Column(name="related_entity")
    private String relatedEntity;

    public UserAction(String author, ActionType actionType) {
        this.author = author;
        this.actionType = actionType;
        this.timestamp = LocalDateTime.now();
    }

    public UserAction(){

    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRelatedEntity() {
        return relatedEntity;
    }

    public void setRelatedEntity(String relatedEntity) {
        this.relatedEntity = relatedEntity;
    }
}
