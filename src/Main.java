import java.util.*;

// -------------------- DOMAIN MODEL --------------------
class Room {
    private String roomType;
    private double price;
    private List<String> amenities;

    public Room(String roomType, double price, List<String> amenities) {
        this.roomType = roomType;
        this.price = price;
        this.amenities = amenities;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    @Override
    public String toString() {
        return "Room Type: " + roomType +
                ", Price: " + price +
                ", Amenities: " + amenities;
    }
}

// -------------------- INVENTORY (STATE HOLDER) --------------------
class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public void addRoom(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    // Defensive copy
    public Map<String, Integer> getAllAvailability() {
        return new HashMap<>(roomAvailability);
    }
}

// -------------------- SEARCH SERVICE (USE CASE 4 REUSED) --------------------
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public List<Room> searchAvailableRooms() {
        List<Room> result = new ArrayList<>();

        Map<String, Integer> availability = inventory.getAllAvailability();

        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            if (entry.getValue() > 0) {
                Room room = roomCatalog.get(entry.getKey());
                if (room != null) {
                    result.add(room);
                }
            }
        }
        return result;
    }
}

// -------------------- RESERVATION (BOOKING REQUEST) --------------------
class Reservation {
    private static int counter = 1;

    private int requestId;
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.requestId = counter++;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "RequestID: " + requestId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// -------------------- BOOKING REQUEST QUEUE --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        this.queue = new LinkedList<>();
    }

    // Add request (FIFO)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request Added: " + reservation);
    }

    // View next request (without removing)
    public Reservation peekRequest() {
        return queue.peek();
    }

    // Get and remove next request (for future use cases)
    public Reservation pollRequest() {
        return queue.poll();
    }

    // Display full queue
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO Order) ---");
        for (Reservation r : queue) {
            System.out.println(r);
        }
    }
}

// -------------------- MAIN APPLICATION --------------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // -------------------- STEP 1: ROOM CATALOG --------------------
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Deluxe",
                new Room("Deluxe", 2500, Arrays.asList("WiFi", "AC", "TV")));

        roomCatalog.put("Suite",
                new Room("Suite", 5000, Arrays.asList("WiFi", "AC", "TV", "Mini Bar")));

        roomCatalog.put("Standard",
                new Room("Standard", 1500, Arrays.asList("WiFi", "Fan")));

        // -------------------- STEP 2: INVENTORY --------------------
        Inventory inventory = new Inventory();
        inventory.addRoom("Deluxe", 2);
        inventory.addRoom("Suite", 1);
        inventory.addRoom("Standard", 3);

        // -------------------- STEP 3: SEARCH AVAILABLE ROOMS --------------------
        SearchService searchService = new SearchService(inventory, roomCatalog);

        System.out.println("Available Rooms:");
        List<Room> availableRooms = searchService.searchAvailableRooms();
        for (Room room : availableRooms) {
            System.out.println(room);
        }

        // -------------------- STEP 4: BOOKING REQUEST QUEUE --------------------
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulating multiple guest booking requests (arrival order matters)
        Reservation r1 = new Reservation("Alice", "Deluxe");
        Reservation r2 = new Reservation("Bob", "Suite");
        Reservation r3 = new Reservation("Charlie", "Deluxe");
        Reservation r4 = new Reservation("David", "Standard");

        // Add to queue (FIFO)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);
        bookingQueue.addRequest(r4);

        // -------------------- STEP 5: DISPLAY QUEUE --------------------
        bookingQueue.displayQueue();

        // -------------------- IMPORTANT NOTE --------------------
        System.out.println("\nNOTE:");
        System.out.println("No inventory updates or room allocation are performed.");
        System.out.println("Requests are only collected and stored in FIFO order.");
    }
}