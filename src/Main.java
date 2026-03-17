import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled = false;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", RoomType: " + roomType +
                ", RoomID: " + roomId +
                ", Status: " + (isCancelled ? "CANCELLED" : "CONFIRMED");
    }
}

class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public void increment(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }

    public Map<String, Integer> getAll() {
        return availability;
    }
}

class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

class CancellationService {

    private Inventory inventory;
    private BookingHistory history;

    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        Reservation reservation = history.getReservation(reservationId);

        if (reservation == null) {
            System.out.println(" Cancellation Failed: Reservation not found.");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println(" Cancellation Failed: Already cancelled.");
            return;
        }

        rollbackStack.push(reservation.getRoomId());

        inventory.increment(reservation.getRoomType());

        reservation.cancel();

        System.out.println("✅ Cancellation Successful: " + reservation);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

public class Main {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        inventory.addRoom("Deluxe", 0);

        // Booking history (simulate confirmed booking)
        BookingHistory history = new BookingHistory();
        Reservation r1 = new Reservation("RES-101", "Alice", "Deluxe", "DEL-12345");
        history.addReservation(r1);

        CancellationService service = new CancellationService(inventory, history);

        service.cancelBooking("RES-101");

        service.cancelBooking("RES-101");

        service.cancelBooking("RES-999");

        service.showRollbackStack();

        System.out.println("\nFinal Inventory: " + inventory.getAll());
    }
}