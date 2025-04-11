package com.example.cydomotix.Model.Objects;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name="PowerChangeEvent")
public class PowerChangeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "connected_object_id", nullable = false)
    private ConnectedObject connectedObject;

    private double power; // en W

    private ZonedDateTime timestamp;


    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
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
