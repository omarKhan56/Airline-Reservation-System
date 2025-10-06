import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class ReservationSystem {
    private Scanner scanner;
    private User currentUser;
    
    public ReservationSystem() {
        this.scanner = new Scanner(System.in);
    }
    
    // Start the reservation system
    public void start() {
        try {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║       🛫 AIRLINE RESERVATION SYSTEM 🛬                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");
            
            // Test database connection
            DatabaseConnection.getConnection();
            
            while (true) {
                if (currentUser == null) {
                    showLoginMenu();
                } else {
                    showMainMenu();
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Database Error: " + e.getMessage());
        } finally {
            scanner.close();
            DatabaseConnection.closeConnection();
        }
    }
    
    // Login/Registration menu
    private void showLoginMenu() throws SQLException {
        System.out.println("\n--- LOGIN / REGISTER ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("👋 Thank you for using Airline Reservation System!");
                    DatabaseConnection.closeConnection();
                    System.exit(0);
                default:
                    System.out.println("❌ Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input! Please enter a number.");
            scanner.nextLine(); // clear buffer
        }
    }
    
    // Login method
    private void login() throws SQLException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        try {
            currentUser = User.loginUser(username, password);
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    // Registration method
    private void register() throws SQLException {
        System.out.println("\n--- USER REGISTRATION ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        
        try {
            User newUser = new User(username, password, fullName, email, phone);
            newUser.registerUser();
            System.out.println("✅ Registration successful! Please login.");
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    // Main menu after login
    private void showMainMenu() throws SQLException {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Welcome, " + currentUser.getFullName() + "!");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("1. Search Flights");
        System.out.println("2. Book Flight");
        System.out.println("3. View Booking");
        System.out.println("4. Cancel Booking");
        System.out.println("5. View Payment");
        System.out.println("6. Logout");
        System.out.print("Enter choice: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    searchFlights();
                    break;
                case 2:
                    bookFlight();
                    break;
                case 3:
                    viewBooking();
                    break;
                case 4:
                    cancelBooking();
                    break;
                case 5:
                    viewPayment();
                    break;
                case 6:
                    logout();
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input! Please enter a number.");
            scanner.nextLine(); // clear buffer
        }
    }
    
    // Search flights
    private void searchFlights() throws SQLException {
        System.out.println("\n--- SEARCH FLIGHTS ---");
        System.out.print("Enter source city: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination city: ");
        String destination = scanner.nextLine();
        
        try {
            List<Flight> flights = Flight.searchFlights(source, destination);
            
            if (flights.isEmpty()) {
                System.out.println("❌ No flights found for this route!");
            } else {
                System.out.println("\n✈️  Available Flights:");
                for (int i = 0; i < flights.size(); i++) {
                    System.out.println("\n--- Flight " + (i + 1) + " ---");
                    flights.get(i).displayFlightDetails();
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching flights: " + e.getMessage());
        }
    }
    
    // Book flight
    private void bookFlight() throws SQLException {
        System.out.println("\n--- BOOK FLIGHT ---");
        System.out.print("Enter source city: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination city: ");
        String destination = scanner.nextLine();
        
        try {
            List<Flight> flights = Flight.searchFlights(source, destination);
            
            if (flights.isEmpty()) {
                System.out.println("❌ No flights available!");
                return;
            }
            
            // Display available flights
            System.out.println("\n✈️  Available Flights:");
            for (int i = 0; i < flights.size(); i++) {
                System.out.println("\n" + (i + 1) + ".");
                flights.get(i).displayFlightDetails();
            }
            
            System.out.print("\nSelect flight number (1-" + flights.size() + "): ");
            int flightChoice = scanner.nextInt();
            scanner.nextLine();
            
            if (flightChoice < 1 || flightChoice > flights.size()) {
                System.out.println("❌ Invalid flight selection!");
                return;
            }
            
            Flight selectedFlight = flights.get(flightChoice - 1);
            
            // Get passenger details
            System.out.println("\n--- PASSENGER DETAILS ---");
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter gender (Male/Female/Other): ");
            String gender = scanner.nextLine();
            System.out.print("Enter passport number: ");
            String passportNumber = scanner.nextLine();
            System.out.print("Enter nationality: ");
            String nationality = scanner.nextLine();
            
            // Create passenger
            Passenger passenger = new Passenger(firstName, lastName, age, gender, passportNumber, nationality);
            passenger.savePassenger();
            
            // Get seat number
            System.out.print("Enter preferred seat number (e.g., A12): ");
            String seatNumber = scanner.nextLine();
            
            // Create booking
            Booking booking = new Booking(currentUser.getUserId(), selectedFlight.getFlightId(), 
                                         passenger.getPassengerId(), seatNumber, selectedFlight.getPrice());
            booking.createBooking();
            
            // Update flight seats
            selectedFlight.updateSeats(1);
            
            // Process payment
            System.out.println("\n--- PAYMENT ---");
            System.out.println("1. Credit Card");
            System.out.println("2. Debit Card");
            System.out.println("3. UPI");
            System.out.println("4. Net Banking");
            System.out.print("Select payment method: ");
            int paymentChoice = scanner.nextInt();
            scanner.nextLine();
            
            String paymentMethod = switch (paymentChoice) {
                case 1 -> "Credit Card";
                case 2 -> "Debit Card";
                case 3 -> "UPI";
                case 4 -> "Net Banking";
                default -> "Cash";
            };
            
            Payment payment = new Payment(booking.getBookingId(), paymentMethod, booking.getTotalAmount());
            payment.processPayment();
            
            System.out.println("\n🎉 Booking completed successfully!");
            System.out.println("Your Booking ID: " + booking.getBookingId());
            
        } catch (SQLException e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Invalid input: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    // View booking
    private void viewBooking() throws SQLException {
        System.out.print("\nEnter booking ID: ");
        try {
            int bookingId = scanner.nextInt();
            scanner.nextLine();
            Booking.viewBooking(bookingId);
        } catch (Exception e) {
            System.out.println("❌ Invalid booking ID!");
            scanner.nextLine();
        }
    }
    
    // Cancel booking
    private void cancelBooking() throws SQLException {
        System.out.print("\nEnter booking ID to cancel: ");
        try {
            int bookingId = scanner.nextInt();
            scanner.nextLine();
            Booking.cancelBooking(bookingId);
        } catch (SQLException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Invalid booking ID!");
            scanner.nextLine();
        }
    }
    
    // View payment
    private void viewPayment() throws SQLException {
        System.out.print("\nEnter booking ID: ");
        try {
            int bookingId = scanner.nextInt();
            scanner.nextLine();
            Payment.viewPayment(bookingId);
        } catch (Exception e) {
            System.out.println("❌ Invalid booking ID!");
            scanner.nextLine();
        }
    }
    
    // Logout
    private void logout() {
        currentUser = null;
        System.out.println("✅ Logged out successfully!");
    }
}