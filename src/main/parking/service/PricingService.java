package parking.service;

import parking.model.ParkingSpot;
import parking.model.Reservation;
import parking.model.Tariff;
import parking.repository.TariffRepository;
import java.time.Duration;
import java.time.LocalDateTime;

public class PricingService {
    private final TariffRepository tariffRepo = new TariffRepository();

    public double calculateCost(int reservationId) {
        Reservation reservation = reservationRepo.findById(reservationId).orElseThrow();
        ParkingSpot spot = spotRepo.findById(reservation.getSpotId()).orElseThrow();
        Tariff tariff = tariffRepo.findBySpotType(spot.getType()).orElseThrow();

        LocalDateTime start = reservation.getStartTime();
        LocalDateTime end = reservation.getEndTime() != null ?
                reservation.getEndTime() : LocalDateTime.now();

        long hours = Duration.between(start, end).toHours();
        if (hours < 1) hours = 1; // Minimum 1 hour

        double cost = hours * tariff.getHourlyRate();
        reservation.setTotalCost(cost);
        reservationRepo.update(reservation);

        return cost;
    }
}