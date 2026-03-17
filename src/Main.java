iimport java.util.*;

// -------------------- RESERVATION --------------------
class Reservation {
    private String guestName;
    private String roomType;
    private String allocatedRoomId;

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

    public void setAllocatedRoomId(String roomId) {
        this.allocatedRoomId = roomId;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                (allocatedRoomId != null ? ", Room ID: " + allocatedRoomId : "");
    }
}

// -------------------- INVENTORY --------------------
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        int count = availability.get(type);
        availability.put(type, count - 1);
    }

    public Map<String, Integer> getAll() {
        return availability;
    }
}

// -------------------- BOOKING SERVICE --------------------
class BookingService {

    private Inventory inventory;

    // Ensures global uniqueness
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track allocations per room type
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    // Core Use Case 6 Logic
    public void processBookings(Queue<Reservation> queue) {

        System.out.println("\n--- Processing Bookings ---");

        while (!queue.isEmpty()) {

            Reservation request = queue.poll(); // FIFO

            String roomType = request.getRoomType();

            // Step 1: Check availability
            if (inventory.getAvailability(roomType) <= 0) {
                System.out.println("❌ Booking Failed: " + request);
                continue;
            }

            // Step 2: Generate unique room ID
            String roomId = generateUniqueRoomId(roomType);

            // Step 3: Assign + track (atomic logic)
            allocatedRoomIds.add(roomId);

            roomAllocations
                    .computeIfAbsent(roomType, k -> new HashSet<>())
                    .add(roomId);

            // Step 4: Update inventory immediately
            inventory.decrement(roomType);

            // Step 5: Confirm reservation
            request.setAllocatedRoomId(roomId);

            System.out.println("✅ Booking Confirmed: " + request);
        }
    }

    // Ensures no duplicate room IDs
    private String generateUniqueRoomId(String roomType) {
        String roomId;

        do {
            roomId = roomType.substring(0, 3).toUpperCase()
                    + "-" + UUID.randomUUID().toString().substring(0, 5);
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    public void displayAllocations() {
        System.out.println("\n--- Final Allocations ---");
        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// -------------------- MAIN --------------------
public class Main {
    public static void main(String[] args) {

        // Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Deluxe", 2);
        inventory.addRoom("Suite", 1);

        // Simulated FIFO Queue (from Use Case 5)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Deluxe"));
        queue.offer(new Reservation("Bob", "Suite"));
        queue.offer(new Reservation("Charlie", "Deluxe"));
        queue.offer(new Reservation("David", "Suite")); // should fail

        // Booking Service
        BookingService service = new BookingService(inventory);

        // Process Bookings
        service.processBookings(queue);

        // Final State
        service.displayAllocations();

        System.out.println("\nRemaining Inventory: " + inventory.getAll());
    }
}

void main() {
}