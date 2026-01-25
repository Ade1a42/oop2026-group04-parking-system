package parking.repository;

import parking.database.IDB;
import parking.database.PostgresDB;
import parking.model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleRepository implements IRepository<Vehicle> {
    private final IDB db = new PostgresDB();

    @Override
    public Vehicle save(Vehicle spot) {
        String sql = "INSERT INTO vehicles (plateNumber, ownerName) VALUES (?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, spot.getPlateNumber());
            pstmt.setString(2, spot.getOwnerName());
            pstmt.executeUpdate();

            // Get generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                spot.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spot;
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        String sql = "SELECT * FROM parking_spots WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("owner_name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                spots.add(new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("owner_name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM parking_spots WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vehicle update(Vehicle spot) {
        String sql = "UPDATE parking_spots SET spot_number = ?, type = ?, status = ?, zone = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, spot.getPlateNumber());
            pstmt.setString(2, spot.getOwnerName());
            pstmt.setInt(3, spot.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spot;
    }

    // EXTRA: Find available spots
    public List<Vehicle> findAvailableSpots() {
        List<Vehicle> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE status = 'AVAILABLE'";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                spots.add(new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("owner_name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
}