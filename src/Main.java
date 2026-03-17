import java.util.*;

// -------------------- CUSTOM EXCEPTIONS --------------------
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class NoAvailabilityException extends Exception {
    public NoAvailabilityException(String message) {
        super(message);
    }
}

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

// -------------------- RESERVATION --------------------
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                (roomId != null ? ", Room ID: " + roomId : "");
    }
}

// -------------------- INVENTORY --------------------
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) throws NoAvailabilityException {
        int count = availability.get(type);

        if (count <= 0) {
            throw new NoAvailabilityException("No rooms available for type: " + type);
        }

        availability.put(type, count - 1);
    }
}

// -------------------- VALIDATOR --------------------
class BookingValidator {

    private Inventory inventory;

    public BookingValidator(Inventory inventory) {
        this.inventory = inventory;
    }

    public void validate(Reservation reservation)
            throws InvalidInputException, InvalidRoomTypeException, NoAvailabilityException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidInputException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidRoomTypeException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new NoAvailabilityException("No availability for room type: " + reservation.getRoomType());
        }
    }
}

// -------------------- BOOKING SERVICE --------------------
class BookingService {

    private Inventory inventory;
    private BookingValidator validator;
    private Set<String> allocatedRoomIds = new HashSet<>();

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
        this.validator = new BookingValidator(inventory);
    }

    public void confirmBooking(Reservation reservation) {

        try {
            // ✅ FAIL-FAST VALIDATION
            validator.validate(reservation);

            // Generate unique room ID
            String roomId = generateRoomId(reservation.getRoomType());

            // Allocate room
            allocatedRoomIds.add(roomId);
            inventory.decrement(reservation.getRoomType());

            reservation.setRoomId(roomId);

            System.out.println("✅ Booking Confirmed: " + reservation);

        } catch (InvalidInputException |
                 InvalidRoomTypeException |
                 NoAvailabilityException e) {

            // ✅ GRACEFUL FAILURE
            System.out.println("❌ Booking Failed: " + e.getMessage());
        }
    }

    private String generateRoomId(String roomType) {
        String id;

        do {
            id = roomType.substring(0, 3).toUpperCase()
                    + "-" + UUID.randomUUID().toString().substring(0, 5);
        } while (allocatedRoomIds.contains(id));

        return id;
    }
}

// -------------------- MAIN --------------------
public class Main {
    public static void main(String[] args) {

        // Setup inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Deluxe", 1);
        inventory.addRoom("Suite", 0);

        // Booking service
        BookingService service = new BookingService(inventory);

        // Test cases
        Reservation valid = new Reservation("Alice", "Deluxe");
        Reservation invalidRoom = new Reservation("Bob", "Penthouse");
        Reservation noAvailability = new Reservation("Charlie", "Suite");
        Reservation emptyName = new Reservation("", "Deluxe");

        // Process bookings
        service.confirmBooking(valid);          // success
        service.confirmBooking(invalidRoom);    // invalid room
        service.confirmBooking(noAvailability); // no availability
        service.confirmBooking(emptyName);      // invalid input

        System.out.println("\nSystem is still running safely after errors.");
    }
}