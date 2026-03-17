import java.util.*;

// -------------------- RESERVATION --------------------
class Reservation {
    private String guestName;
    private String roomType;

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

    @Override
    public String toString() {
        return "Guest: " + guestName + ", RoomType: " + roomType;
    }
}

// -------------------- INVENTORY (SHARED RESOURCE) --------------------
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public Inventory() {
        availability.put("Deluxe", 2);
    }

    // Critical section (thread-safe)
    public synchronized boolean allocateRoom(String roomType) {

        int count = availability.getOrDefault(roomType, 0);

        if (count <= 0) {
            return false;
        }

        // Simulate delay (to expose race conditions if unsynchronized)
        try { Thread.sleep(50); } catch (InterruptedException e) {}

        availability.put(roomType, count - 1);
        return true;
    }

    public synchronized int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }
}

// -------------------- BOOKING PROCESSOR --------------------
class BookingProcessor implements Runnable {

    private Queue<Reservation> queue;
    private Inventory inventory;

    public BookingProcessor(Queue<Reservation> queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            Reservation reservation;

            // Synchronized access to shared queue
            synchronized (queue) {
                if (queue.isEmpty()) break;
                reservation = queue.poll();
            }

            if (reservation != null) {
                process(reservation);
            }
        }
    }

    private void process(Reservation reservation) {

        boolean success = inventory.allocateRoom(reservation.getRoomType());

        if (success) {
            System.out.println(Thread.currentThread().getName() +
                    " ✅ Booked: " + reservation);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " ❌ Failed (No Availability): " + reservation);
        }
    }
}

// -------------------- MAIN --------------------
public class Main {
    public static void main(String[] args) throws InterruptedException {

        // Shared resources
        Queue<Reservation> queue = new LinkedList<>();
        Inventory inventory = new Inventory();

        // Simulate multiple booking requests
        for (int i = 1; i <= 5; i++) {
            queue.offer(new Reservation("Guest-" + i, "Deluxe"));
        }

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(queue, inventory), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        t1.join();
        t2.join();
        t3.join();

        // Final state
        System.out.println("\nFinal Availability: " +
                inventory.getAvailability("Deluxe"));
    }
}