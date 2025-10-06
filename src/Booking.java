import java.sql.*;

public class Booking {
    private int bookingId;
    private int userId;
    private int flightId;
    private int passengerId;
    private String seatNumber;
    private String bookingStatus;
    private double totalAmount;
    
    // Constructor
    public Booking(int userId, int flightId, int passengerId, String seatNumber, double totalAmount) {
        this.userId = userId;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNumber = seatNumber;
        this.totalAmount = totalAmount;
        this.bookingStatus = "CONFIRMED";
    }
    
    // Getters
    public int getBookingId() { return bookingId; }
    public double getTotalAmount() { return totalAmount; }
    
    // Create booking
    public void createBooking() throws SQLException {
        String query = "INSERT INTO bookings (user_id, flight_id, passenger_id, seat_number, booking_status, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, flightId);
            pstmt.setInt(3, passengerId);
            pstmt.setString(4, seatNumber);
            pstmt.setString(5, bookingStatus);
            pstmt.setDouble(6, totalAmount);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.bookingId = rs.getInt(1);
                }
                
                conn.commit(); // Commit transaction
                System.out.println("✅ Booking created successfully! Booking ID: " + bookingId);
            } else {
                conn.rollback();
                throw new SQLException("Booking creation failed!");
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Booking transaction failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }
    
    // View booking details
    public static void viewBooking(int bookingId) throws SQLException {
        String query = "SELECT b.*, u.full_name, f.flight_number, f.airline_name, f.source_city, f.destination_city, " +
                       "p.first_name, p.last_name FROM bookings b " +
                       "JOIN users u ON b.user_id = u.user_id " +
                       "JOIN flights f ON b.flight_id = f.flight_id " +
                       "JOIN passengers p ON b.passenger_id = p.passenger_id " +
                       "WHERE b.booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n╔═══════════════════════ BOOKING DETAILS ════════════════════════╗");
                System.out.println("  Booking ID: " + rs.getInt("booking_id"));
                System.out.println("  Passenger: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("  User: " + rs.getString("full_name"));
                System.out.println("  Flight: " + rs.getString("flight_number") + " (" + rs.getString("airline_name") + ")");
                System.out.println("  Route: " + rs.getString("source_city") + " → " + rs.getString("destination_city"));
                System.out.println("  Seat Number: " + rs.getString("seat_number"));
                System.out.println("  Status: " + rs.getString("booking_status"));
                System.out.println("  Total Amount: ₹" + rs.getDouble("total_amount"));
                System.out.println("  Booking Date: " + rs.getTimestamp("booking_date"));
                System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            } else {
                System.out.println("❌ Booking not found!");
            }
        }
    }
    
    // Cancel booking
    public static void cancelBooking(int bookingId) throws SQLException {
        String query = "UPDATE bookings SET booking_status = 'CANCELLED' WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, bookingId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Booking cancelled successfully!");
            } else {
                throw new SQLException("Booking cancellation failed!");
            }
        }
    }
}