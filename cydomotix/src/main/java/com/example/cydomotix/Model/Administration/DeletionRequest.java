package com.example.cydomotix.Model.Administration;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.ObjectType;
import com.example.cydomotix.Model.Users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name="DeletionRequest")
public class DeletionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_del_request", nullable = false)
    private Integer id;

    private String reason; // commentaire

    @ManyToOne
    @JoinColumn(name = "connected_object_id")
    private ConnectedObject connectedObject;

    @ManyToOne
    @JoinColumn(name = "object_type_id")
    private ObjectType objectType;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private DeletionTargetType targetType;

    @ManyToOne
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requester;

    @Column(name="request_date")
    private ZonedDateTime requestDate;

    public Integer getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ConnectedObject getConnectedObject() {
        return connectedObject;
    }

    public void setConnectedObject(ConnectedObject connectedObject) {
        this.connectedObject = connectedObject;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public ZonedDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public DeletionTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(DeletionTargetType targetType) {
        this.targetType = targetType;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public boolean isForConnectedObject() {
        return targetType == DeletionTargetType.CONNECTED_OBJECT;
    }

    public boolean isForObjectType() {
        return targetType == DeletionTargetType.OBJECT_TYPE;
    }
}
