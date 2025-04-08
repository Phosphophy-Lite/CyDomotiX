package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="UsageEvent")
public class UsageEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    @ManyToOne
    private ConnectedObject connectedObject;*/

    private boolean status; // true = ON, false = OFF

    private LocalDateTime timestamp;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /*
    public ConnectedObject getConnectedObject() {
        return connectedObject;
    }

    public void setConnectedObject(ConnectedObject connectedObject) {
        this.connectedObject = connectedObject;
    }*/

    public Integer getId() {
        return id;
    }
}
