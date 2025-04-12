package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name="UsageEvent")
public class UsageEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "connected_object_id", nullable = false)
    private ConnectedObject connectedObject;

    private boolean status; // true = ON, false = OFF

    private ZonedDateTime timestamp;

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public ConnectedObject getConnectedObject() {
        return connectedObject;
    }

    public void setConnectedObject(ConnectedObject connectedObject) {
        this.connectedObject = connectedObject;
    }

    public Integer getId() {
        return id;
    }
}
