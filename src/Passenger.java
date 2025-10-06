import java.sql.*;

public class Passenger {
    private int passengerId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String passportNumber;
    private String nationality;
    
    // Constructor
    public Passenger(String firstName, String lastName, int age, String gender, 
                     String passportNumber, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
    }
    
    // Getters
    public int getPassengerId() { return passengerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    
    // Save passenger to database
    public void savePassenger() throws SQLException {
        String query = "INSERT INTO passengers (first_name, last_name, age, gender, passport_number, nationality) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, age);
            pstmt.setString(4, gender);
            pstmt.setString(5, passportNumber);
            pstmt.setString(6, nationality);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.passengerId = rs.getInt(1);
                }
                System.out.println("✅ Passenger details saved! Passenger ID: " + passengerId);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to save passenger: " + e.getMessage());
        }
    }
    
    // Display passenger details
    public void displayPassengerDetails() {
        System.out.println("\n--- Passenger Details ---");
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Passport: " + passportNumber);
        System.out.println("Nationality: " + nationality);
    }
}