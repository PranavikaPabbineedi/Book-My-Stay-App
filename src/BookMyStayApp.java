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
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Room Size: " + squareFeet + " sq ft");
        System.out.println("Price per Night: ₹" + pricePerNight);
    }
}

class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 200, 1500);
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 350, 2500);
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 600, 5000);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

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