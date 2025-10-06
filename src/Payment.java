import java.sql.*;
import java.util.UUID;

public class Payment {
    private int paymentId;
    private int bookingId;
    private String paymentMethod;
    private double amount;
    private String paymentStatus;
    private String transactionId;
    
    // Constructor
    public Payment(int bookingId, String paymentMethod, double amount) {
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = "SUCCESS";
        this.transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    // Process payment
    public void processPayment() throws SQLException {
        String query = "INSERT INTO payments (booking_id, payment_method, amount, payment_status, transaction_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, bookingId);
            pstmt.setString(2, paymentMethod);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, paymentStatus);
            pstmt.setString(5, transactionId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.paymentId = rs.getInt(1);
                }
                
                System.out.println("\n╔═══════════════════════ PAYMENT RECEIPT ════════════════════════╗");
                System.out.println("  Payment ID: " + paymentId);
                System.out.println("  Booking ID: " + bookingId);
                System.out.println("  Transaction ID: " + transactionId);
                System.out.println("  Payment Method: " + paymentMethod);
                System.out.println("  Amount Paid: ₹" + amount);
                System.out.println("  Status: " + paymentStatus);
                System.out.println("  ✅ Payment processed successfully!");
                System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            } else {
                throw new SQLException("Payment processing failed!");
            }
        } catch (SQLException e) {
            this.paymentStatus = "FAILED";
            throw new SQLException("Payment failed: " + e.getMessage());
        }
    }
    
    // View payment details
    public static void viewPayment(int bookingId) throws SQLException {
        String query = "SELECT * FROM payments WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n--- Payment Details ---");
                System.out.println("Payment ID: " + rs.getInt("payment_id"));
                System.out.println("Transaction ID: " + rs.getString("transaction_id"));
                System.out.println("Method: " + rs.getString("payment_method"));
                System.out.println("Amount: ₹" + rs.getDouble("amount"));
                System.out.println("Status: " + rs.getString("payment_status"));
                System.out.println("Date: " + rs.getTimestamp("payment_date"));
            } else {
                System.out.println("❌ No payment found for this booking!");
            }
        }
    }
}