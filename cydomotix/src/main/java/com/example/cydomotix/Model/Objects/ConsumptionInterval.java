package com.example.cydomotix.Model.Objects;

import java.time.LocalDateTime;

public class ConsumptionInterval {

    private LocalDateTime start;
    private LocalDateTime end;
    private long durationMinutes;
    private double energyWh;
    private boolean status;

    public ConsumptionInterval(LocalDateTime start, LocalDateTime end, long durationMinutes, double energyWh, boolean status) {
        this.start = start;
        this.end = end;
        this.durationMinutes = durationMinutes;
        this.energyWh = energyWh;
        this.status = status;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
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
