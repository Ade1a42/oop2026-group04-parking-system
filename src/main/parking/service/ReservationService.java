package parking.service;

import parking.model.Reservation;
import parking.model.Vehicle;
import parking.model.ParkingSpot;
import parking.repository.ParkingSpotRepository;
import parking.repository.VehicleRepository;
import parking.repository.ReservationRepository;
import parking.exception.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {
    private final ParkingSpotRepository spotRepo = new ParkingSpotRepository();
    private final VehicleRepository vehicleRepo = new VehicleRepository();
    private final ReservationRepository reservationRepo = new ReservationRepository();

    // USER STORY 1: Reserve a spot
    public Reservation reserveSpot(String plateNumber, String spotType, String zone)
            throws NoFreeSpots, InvalidVehiclePlate {

        // 1. Validate plate
        if (plateNumber == null || plateNumber.length() < 3) {
            throw new InvalidVehiclePlate("Invalid plate: " + plateNumber);
        }

        // 2. Find or create vehicle
        Vehicle vehicle = vehicleRepo.findByPlate(plateNumber)
                .orElse(new Vehicle(plateNumber, "Guest"));
        if (vehicle.getId() == 0) {
            vehicle = vehicleRepo.save(vehicle);
        }

        // 3. Find available spot
        List<ParkingSpot> availableSpots = spotRepo.findAvailableByTypeAndZone(spotType, zone);
        if (availableSpots.isEmpty()) {
            throw new NoFreeSpots("No " + spotType + " spots available in " + zone);
        }

        // 4. Take first available spot
        ParkingSpot spot = availableSpots.get(0);
        spot.setStatus("RESERVED");
        spotRepo.update(spot);

        // 5. Create reservation
        Reservation reservation = new Reservation(vehicle.getId(), spot.getId());
        return reservationRepo.save(reservation);
    }

    // USER STORY 2: Release a spot
    public double releaseSpot(int reservationId) throws ReservationExpired {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if ("COMPLETED".equals(reservation.getStatus())) {
            throw new ReservationExpired("Reservation already completed");
        }

        // Mark as completed
        reservation.setEndTime(LocalDateTime.now());
        reservation.setStatus("COMPLETED");

        // Free the spot
        ParkingSpot spot = spotRepo.findById(reservation.getSpotId()).orElseThrow();
        spot.setStatus("AVAILABLE");
        spotRepo.update(spot);

        return reservation.getTotalCost();
    }

    // USER STORY 3: List free spots
    public List<ParkingSpot> getFreeSpots() {
        return spotRepo.findAvailableSpots();
    }
}