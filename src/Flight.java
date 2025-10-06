import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    private int flightId;
    private String flightNumber;
    private String airlineName;
    private String sourceCity;
    private String destinationCity;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private double price;
    private String status;
    
    // Constructor
    public Flight(int flightId, String flightNumber, String airlineName, String sourceCity, 
                  String destinationCity, Timestamp departureTime, Timestamp arrivalTime,
                  int totalSeats, int availableSeats, double price, String status) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.airlineName = airlineName;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
        this.status = status;
    }
    
    // Getters
    public int getFlightId() { return flightId; }
    public String getFlightNumber() { return flightNumber; }
    public String getAirlineName() { return airlineName; }
    public String getSourceCity() { return sourceCity; }
    public String getDestinationCity() { return destinationCity; }
    public int getAvailableSeats() { return availableSeats; }
    public double getPrice() { return price; }
    
    // Search flights by source and destination
    public static List<Flight> searchFlights(String source, String destination) throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flights WHERE source_city = ? AND destination_city = ? AND status = 'ACTIVE' AND available_seats > 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, source);
            pstmt.setString(2, destination);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                flights.add(new Flight(
                    rs.getInt("flight_id"),
                    rs.getString("flight_number"),
                    rs.getString("airline_name"),
                    rs.getString("source_city"),
                    rs.getString("destination_city"),
                    rs.getTimestamp("departure_time"),
                    rs.getTimestamp("arrival_time"),
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats"),
                    rs.getDouble("price"),
                    rs.getString("status")
                ));
            }
        }
        return flights;
    }
    
    // Display flight details
    public void displayFlightDetails() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("  Flight Number: " + flightNumber);
        System.out.println("  Airline: " + airlineName);
        System.out.println("  Route: " + sourceCity + " → " + destinationCity);
        System.out.println("  Departure: " + departureTime);
        System.out.println("  Arrival: " + arrivalTime);
        System.out.println("  Available Seats: " + availableSeats + "/" + totalSeats);
        System.out.println("  Price: ₹" + price);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    // Update available seats after booking
    public void updateSeats(int bookedSeats) throws SQLException {
        String query = "UPDATE flights SET available_seats = available_seats - ? WHERE flight_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, bookedSeats);
            pstmt.setInt(2, flightId);
            pstmt.executeUpdate();
        }
    }
}