package sfs.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateTennisCourtRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Positive
    private double pricePerHour;

    @NotNull
    @Positive
    private int capacity;

    @NotBlank
    @Size(min = 4, max = 5)
    private String surfaceType;

    @NotNull
    private Boolean isIndoor;

    public CreateTennisCourtRequest() {
    }

    public CreateTennisCourtRequest(String name, double pricePerHour, int capacity, String surfaceType, Boolean isIndoor) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.surfaceType = surfaceType;
        this.isIndoor = isIndoor;
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

    public String getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(String surfaceType) {
        this.surfaceType = surfaceType;
    }

    public Boolean getIsIndoor() {
        return isIndoor;
    }

    public void setIsIndoor(Boolean isIndoor) {
        this.isIndoor = isIndoor;
    }
}
