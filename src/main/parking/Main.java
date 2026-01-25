package parking;

import parking.database.DatabaseConnection;
import parking.repository.ParkingSpotRepository;
import parking.repository.VehicleRepository;
import parking.service.ParkingSpotService;
import parking.service.VehicleService;
import parking.model.ParkingSpot;
import parking.model.Vehicle;
import parking.exception.InvalidVehiclePlate;
import parking.exception.NoFreeSpots;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== PARKING MANAGEMENT SYSTEM ===\n");

        try {
            // Get database instance
            DatabaseConnection db = DatabaseConnection.getInstance();

            // Create repositories with database connection
            ParkingSpotRepository spotRepo = new ParkingSpotRepository(db);
            VehicleRepository vehicleRepo = new VehicleRepository(db);

            // Create services
            ParkingSpotService spotService = new ParkingSpotService(spotRepo);
            VehicleService vehicleService = new VehicleService(vehicleRepo);

            // VEHICLE OPERATIONS
            System.out.println("=== Vehicle Operations ===");

            // Register a vehicle
            try {
                Vehicle vehicle1 = vehicleService.registerVehicle("ABC123", "John Doe");
                System.out.println("✓ Registered vehicle: " + vehicle1.getPlateNumber() + " (ID: " + vehicle1.getId() + ")");
            } catch (InvalidVehiclePlate e) {
                System.out.println("✗ Error: " + e.getMessage());
            }

            // List all vehicles
            System.out.println("\nAll vehicles:");
            for (Vehicle v : vehicleService.getAllVehicles()) {
                System.out.println("  " + v.getId() + ": " + v.getPlateNumber() + " - " + v.getOwnerName());
            }

            // PARKING SPOT OPERATIONS
            System.out.println("\n=== Parking Spot Operations ===");

            // List all parking spots
            System.out.println("All parking spots:");
            for (ParkingSpot spot : spotService.getAllSpots()) {
                System.out.println("  " + spot);
            }

            // Get available spots
            try {
                System.out.println("\nAvailable parking spots:");
                for (ParkingSpot spot : spotService.getAvailableSpots()) {
                    System.out.println("Available: " + spot);
                }
            } catch (NoFreeSpots e) {
                System.out.println("NoFreeSpots: " + e.getMessage());
            }

            // Add a new parking spot
            System.out.println("\nAdding new parking spot...");
            ParkingSpot newSpot = spotService.addParkingSpot("B2", "STANDARD", "ZONE_B");
            if (newSpot != null) {
                System.out.println("Added: " + newSpot);
            }

            // Find spot by ID
            ParkingSpot foundSpot = spotService.getSpotById(1);
            if (foundSpot != null) {
                System.out.println("\nFound spot by ID (1): " + foundSpot);
            }

            System.out.println("\n===  System Ready! ===");

        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}