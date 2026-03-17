import java.util.*;

// -------------------- RESERVATION --------------------
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId;
    }
}

// -------------------- BOOKING HISTORY --------------------
class BookingHistory {

    // List preserves insertion order (chronological)
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to History: " + reservation);
    }

    // Read-only access (defensive copy)
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history);
    }
}

// -------------------- REPORT SERVICE --------------------
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void showAllBookings() {
        System.out.println("\n--- Booking History ---");

        List<Reservation> reservations = history.getAllReservations();

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        List<Reservation> reservations = history.getAllReservations();

        System.out.println("\n--- Booking Summary Report ---");

        // Total bookings
        System.out.println("Total Bookings: " + reservations.size());

        // Count per room type
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("Bookings by Room Type:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// -------------------- MAIN --------------------
public class Main {
    public static void main(String[] args) {

        // Booking History
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("RES-101", "Alice", "Deluxe", "DEL-12345");
        Reservation r2 = new Reservation("RES-102", "Bob", "Suite", "SUI-54321");
        Reservation r3 = new Reservation("RES-103", "Charlie", "Deluxe", "DEL-67890");

        // Add to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin views reports
        BookingReportService reportService = new BookingReportService(history);

        reportService.showAllBookings();
        reportService.generateSummaryReport();

        // IMPORTANT NOTE
        System.out.println("\nNOTE:");
        System.out.println("Reporting does not modify booking history.");
    }
}