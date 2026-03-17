iimport java.util.*;

// -------------------- RESERVATION --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// -------------------- ADD-ON SERVICE --------------------
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// -------------------- ADD-ON SERVICE MANAGER --------------------
class AddOnServiceManager {

    // Map<ReservationID, List<Services>>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add services to a reservation
    public void addServices(String reservationId, List<AddOnService> services) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .addAll(services);

        System.out.println("Services added to Reservation " + reservationId + ": " + services);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;

        for (AddOnService service : getServices(reservationId)) {
            total += service.getCost();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        System.out.println("\nAdd-On Services for Reservation " + reservationId + ":");

        if (services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// -------------------- MAIN --------------------
public class Main {
    public static void main(String[] args) {

        // Existing confirmed reservation (from Use Case 6)
        Reservation reservation = new Reservation("RES-101", "Alice", "Deluxe");

        System.out.println("Booking Confirmed:");
        System.out.println(reservation);

        // Create add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService spa = new AddOnService("Spa Access", 2000);

        // Add-On Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addServices(
                reservation.getReservationId(),
                Arrays.asList(breakfast, airportPickup)
        );

        // Add more services later (extensibility)
        manager.addServices(
                reservation.getReservationId(),
                Arrays.asList(spa)
        );

        // Display selected services and cost
        manager.displayServices(reservation.getReservationId());

        // IMPORTANT: No inventory or booking logic is modified
        System.out.println("\nNOTE:");
        System.out.println("Core booking and inventory remain unchanged.");
    }
}

void main() {
}