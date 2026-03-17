import java.util.HashMap;
import java.util.Map;

// Abstract Room Class
abstract class Room {

    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.0);
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.0);
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.0);
    }
}

// Inventory Class
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

// Main Class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Hotel Room Inventory Status\n");

        System.out.println("Single Room:");
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + availability.get("Single Room"));
        System.out.println();

        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + availability.get("Double Room"));
        System.out.println();

        System.out.println("Suite Room:");
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + availability.get("Suite Room"));
    }
}

// Suite Room Class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 600, 5000);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create room objects using polymorphism
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("----- Single Room -----");
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);

        System.out.println("\n----- Double Room -----");
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);

        System.out.println("\n----- Suite Room -----");
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
    }
}