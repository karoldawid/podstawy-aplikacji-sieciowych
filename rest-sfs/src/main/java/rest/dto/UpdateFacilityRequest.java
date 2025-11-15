package rest.dto;

public class UpdateFacilityRequest {
    private String name;
    private double pricePerHour;
    private int capacity;

    public UpdateFacilityRequest(String name, double pricePerHour, int capacity) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
