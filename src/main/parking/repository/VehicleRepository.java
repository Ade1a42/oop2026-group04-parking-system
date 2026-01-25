package parking.repository;

import parking.database.IDB;
import parking.model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository {
    private final IDB db;

    public VehicleRepository(IDB db) {
        this.db = db;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (plate_number, owner_name) VALUES (?, ?) RETURNING id";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getPlateNumber());
            stmt.setString(2, vehicle.getOwnerName());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                vehicle.setId(rs.getInt("id"));
            }
            return vehicle;

        } catch (SQLException e) {
            System.err.println("Error creating vehicle: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Vehicle findById(int id) {
        String sql = "SELECT * FROM vehicles WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("owner_name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding vehicle: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Vehicle findByPlateNumber(String plateNumber) {
        String sql = "SELECT * FROM vehicles WHERE plate_number = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plateNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("owner_name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding vehicle by plate: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY id";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("owner_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET plate_number = ?, owner_name = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getPlateNumber());
            stmt.setString(2, vehicle.getOwnerName());
            stmt.setInt(3, vehicle.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return vehicle;
            }
        } catch (SQLException e) {
            System.err.println("Error updating vehicle: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
            return false;
        }
    }
}