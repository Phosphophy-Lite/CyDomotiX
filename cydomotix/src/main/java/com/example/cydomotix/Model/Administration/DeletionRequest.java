package com.example.cydomotix.Model.Administration;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requester;

    @Column(name="request_date")
    private LocalDateTime requestDate;

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

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
