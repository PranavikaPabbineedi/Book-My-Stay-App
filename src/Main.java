import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", RoomType: " + roomType +
                ", RoomID: " + roomId;
    }
}

class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public Map<String, Integer> getAvailabilityMap() {
        return availability;
    }

    @Override
    public String toString() {
        return availability.toString();
    }
}

class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Collection<Reservation> getAllReservations() {
        return reservations.values();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Reservation r : reservations.values()) {
            sb.append(r).append("\n");
        }
        return sb.toString();
    }
}

class PersistenceService {

    private static final String FILE_PATH = "hotel_data.ser";

    public static void saveState(BookingHistory history, Inventory inventory) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(history);
            out.writeObject(inventory);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save state: " + e.getMessage());
        }
    }

    public static State loadState() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return new State(new BookingHistory(), new Inventory());
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            BookingHistory history = (BookingHistory) in.readObject();
            Inventory inventory = (Inventory) in.readObject();
            System.out.println("System state restored successfully.");
            return new State(history, inventory);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to restore state: " + e.getMessage());
            System.out.println("Starting with fresh state.");
            return new State(new BookingHistory(), new Inventory());
        }
    }

    public static class State {
        public BookingHistory history;
        public Inventory inventory;

        public State(BookingHistory history, Inventory inventory) {
            this.history = history;
            this.inventory = inventory;
        }
    }
}

public class Main {
    public static void main(String[] args) {

        PersistenceService.State state = PersistenceService.loadState();
        BookingHistory history = state.history;
        Inventory inventory = state.inventory;

        System.out.println("\n--- Current Inventory ---\n" + inventory);
        System.out.println("--- Booking History ---\n" + history);

        Reservation r1 = new Reservation("RES-201", "Alice", "Deluxe", "DEL-111");
        Reservation r2 = new Reservation("RES-202", "Bob", "Suite", "SUI-222");

        history.addReservation(r1);
        history.addReservation(r2);
        inventory.addRoom("Deluxe", 1);
        inventory.addRoom("Suite", 2);

        System.out.println("\n--- Updated Inventory ---\n" + inventory);
        System.out.println("--- Updated Booking History ---\n" + history);

        PersistenceService.saveState(history, inventory);

        System.out.println("\nSystem ready for next restart with persisted data.");
    }
}