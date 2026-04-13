package sfs.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateSwimmingPoolRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Positive
    private double pricePerHour;

    @NotNull
    @Positive
    private int capacity;

    @NotNull
    private Integer poolLength;

    @NotNull
    private Integer numberOfLanes;

    public CreateSwimmingPoolRequest() {
    }

    public CreateSwimmingPoolRequest(String name, double pricePerHour, int capacity, Integer poolLength, Integer numberOfLanes) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.poolLength = poolLength;
        this.numberOfLanes = numberOfLanes;
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

    public Integer getPoolLength() {
        return poolLength;
    }

    public void setPoolLength(Integer poolLength) {
        this.poolLength = poolLength;
    }

    public Integer getNumberOfLanes() {
        return numberOfLanes;
    }

    public void setNumberOfLanes(Integer numberOfLanes) {
        this.numberOfLanes = numberOfLanes;
    }
}
