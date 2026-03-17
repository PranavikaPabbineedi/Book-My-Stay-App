import java.util.List;

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