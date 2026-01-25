package parking.model;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private int vehicleId;
    private int spotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private String status;  // active, completed, canceled


    public Reservation() {}
    public Reservation(int vehicleId, int spotId) {
        this.vehicleId = vehicleId;
        this.spotId = spotId;
        this.startTime = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // setters and getters

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
