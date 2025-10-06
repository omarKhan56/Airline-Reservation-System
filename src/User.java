import java.sql.*;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    
    // Constructor
    public User(String username, String password, String fullName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    
    // Register new user
    public void registerUser() throws SQLException {
        String query = "INSERT INTO users (username, password, full_name, email, phone) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.userId = rs.getInt(1);
                }
                System.out.println("✅ User registered successfully! User ID: " + userId);
            }
        } catch (SQLException e) {
            throw new SQLException("Registration failed: " + e.getMessage());
        }
    }
    
    // Login user
    public static User loginUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                user.setUserId(rs.getInt("user_id"));
                System.out.println("✅ Login successful! Welcome " + user.getFullName());
                return user;
            } else {
                throw new SQLException("Invalid username or password!");
            }
        }
    }
}