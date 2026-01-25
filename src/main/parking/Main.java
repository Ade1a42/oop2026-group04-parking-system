package parking;

import parking.service.ReservationService;
import parking.service.PricingService;
import parking.exception.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReservationService reservationService = new ReservationService();
        PricingService pricingService = new PricingService();

        System.out.println("SMART PARKING SYSTEM");

        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. View available spots");
            System.out.println("2. Reserve a spot");
            System.out.println("3. Release a spot (calculate cost)");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number!");
                continue;
            }

            switch (choice) {
                case 1:
                    // User Story: List free spots
                    System.out.println("\nAvailable spots:");
                    reservationService.getFreeSpots().forEach(spot -> {
                        System.out.printf("- %s (%s) in %s%n",
                                spot.getSpotNumber(), spot.getType(), spot.getZone());
                    });
                    break;

                case 2:
                    // User Story: Reserve a spot
                    try {
                        System.out.print("Enter vehicle plate: ");
                        String plate = scanner.nextLine();
                        System.out.print("Enter spot type (STANDARD/ELECTRIC/DISABLED): ");
                        String type = scanner.nextLine().toUpperCase();
                        System.out.print("Enter zone (ZONE_A/ZONE_B): ");
                        String zone = scanner.nextLine();

                        var reservation = reservationService.reserveSpot(plate, type, zone);
                        System.out.println("Spot reserved successfully!");
                        System.out.println("Reservation ID: " + reservation.getId());

                    } catch (NoFreeSpots e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (InvalidVehiclePlate e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    // User Story: Release spot & calculate cost
                    try {
                        System.out.print("Enter reservation ID to release: ");
                        int reservationId = Integer.parseInt(scanner.nextLine());

                        double cost = pricingService.calculateCost(reservationId);
                        reservationService.releaseSpot(reservationId);

                        System.out.printf("Spot released! Total cost: $%.2f%n", cost);

                    } catch (ReservationExpired e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}