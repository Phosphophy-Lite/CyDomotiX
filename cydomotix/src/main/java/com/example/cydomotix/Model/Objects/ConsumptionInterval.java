package com.example.cydomotix.Model.Objects;

import java.time.ZonedDateTime;

public class ConsumptionInterval {

    private ZonedDateTime start;
    private ZonedDateTime end;
    private long durationMinutes;
    private double energyWh;
    private boolean status;

    public ConsumptionInterval(ZonedDateTime start, ZonedDateTime end, long durationMinutes, double energyWh, boolean status) {
        this.start = start;
        this.end = end;
        this.durationMinutes = durationMinutes;
        this.energyWh = energyWh;
        this.status = status;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getEnergyWh() {
        return energyWh;
    }

    public void setEnergyWh(double energyWh) {
        this.energyWh = energyWh;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
