package parking.repository;

import parking.database.IDB;
import parking.database.PostgresDB;
import parking.model.ParkingSpot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingSpotRepository implements IRepository<ParkingSpot> {
    private final IDB db = new PostgresDB();

    @Override
    public ParkingSpot save(ParkingSpot spot) {
        String sql = "INSERT INTO parking_spots (spot_number, type, status, zone) VALUES (?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, spot.getSpotNumber());
            pstmt.setString(2, spot.getType());
            pstmt.setString(3, spot.getStatus());
            pstmt.setString(4, spot.getZone());
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
    public Optional<ParkingSpot> findById(int id) {
        String sql = "SELECT * FROM parking_spots WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new ParkingSpot(
                        rs.getInt("id"),
                        rs.getString("spot_number"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("zone")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<ParkingSpot> findAll() {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                spots.add(new ParkingSpot(
                        rs.getInt("id"),
                        rs.getString("spot_number"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("zone")
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
    public ParkingSpot update(ParkingSpot spot) {
        String sql = "UPDATE parking_spots SET spot_number = ?, type = ?, status = ?, zone = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, spot.getSpotNumber());
            pstmt.setString(2, spot.getType());
            pstmt.setString(3, spot.getStatus());
            pstmt.setString(4, spot.getZone());
            pstmt.setInt(5, spot.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spot;
    }

    // EXTRA: Find available spots
    public List<ParkingSpot> findAvailableSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE status = 'AVAILABLE'";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                spots.add(new ParkingSpot(
                        rs.getInt("id"),
                        rs.getString("spot_number"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("zone")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
}