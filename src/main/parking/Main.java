package parking;

import parking.database.DatabaseConnection;
import parking.database.IDB;
import parking.repository.ParkingSpotRepository;
import parking.repository.VehicleRepository;
import parking.service.ParkingSpotService;
import parking.service.VehicleService;
import parking.model.ParkingSpot;
import parking.model.Vehicle;
import parking.exception.InvalidVehiclePlate;
import parking.exception.NoFreeSpots;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SMART PARKING MANAGEMENT SYSTEM ===\n");

        try {
            IDB db = new DatabaseConnection();

            ParkingSpotRepository spotRepo = new ParkingSpotRepository(db);
            VehicleRepository vehicleRepo = new VehicleRepository(db);

            ParkingSpotService spotService = new ParkingSpotService(spotRepo);
            VehicleService vehicleService = new VehicleService(vehicleRepo);

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Register a vehicle");
                System.out.println("2. List all vehicles");
                System.out.println("3. Add a parking spot");
                System.out.println("4. List all parking spots");
                System.out.println("5. Show available parking spots");
                System.out.println("6. Exit");
                System.out.print("Enter option: ");

                String input = scanner.nextLine();

                switch (input) {
                    case "1":
                        System.out.print("Enter vehicle plate: ");
                        String plate = scanner.nextLine();
                        System.out.print("Enter owner name: ");
                        String owner = scanner.nextLine();
                        try {
                            Vehicle vehicle = vehicleService.registerVehicle(plate, owner);
                            System.out.println("✓ Registered: " + vehicle.getPlateNumber() + " (ID: " + vehicle.getId() + ")");
                        } catch (InvalidVehiclePlate e) {
                            System.out.println("✗ Error: " + e.getMessage());
                        }
                        break;

                    case "2":
                        System.out.println("\nAll vehicles:");
                        for (Vehicle v : vehicleService.getAllVehicles()) {
                            System.out.println("  " + v.getId() + ": " + v.getPlateNumber() + " - " + v.getOwnerName());
                        }
                        break;

                    case "3":
                        System.out.print("Enter spot code: ");
                        String code = scanner.nextLine();
                        System.out.print("Enter type (STANDARD/VIP): ");
                        String type = scanner.nextLine();
                        System.out.print("Enter zone: ");
                        String zone = scanner.nextLine();
                        ParkingSpot newSpot = spotService.addParkingSpot(code, type, zone);
                        System.out.println("Added parking spot: " + newSpot);
                        break;

                    case "4":
                        System.out.println("\nAll parking spots:");
                        for (ParkingSpot spot : spotService.getAllSpots()) {
                            System.out.println("  " + spot);
                        }
                        break;
                    case "5":
                        try {
                            System.out.println("\nAvailable parking spots:");
                            for (ParkingSpot spot : spotService.getAvailableSpots()) {
                                System.out.println("Available: " + spot);
                            }
                        } catch (NoFreeSpots e) {
                            System.out.println("NoFreeSpots: " + e.getMessage());
                        }
                        break;

                    case "6":
                        running = false;
                        System.out.println("Exiting system. Bye!");
                        break;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}