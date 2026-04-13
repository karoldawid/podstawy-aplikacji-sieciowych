package sfs.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpdateSwimmingPoolRequest {

    @NotBlank @Size(min = 3, max = 20)
    private String name;

    @NotNull @Positive
    private Double pricePerHour;

    @NotNull @Positive
    private Integer capacity;

    @NotNull @Positive
    private Integer poolLength;

    @NotNull @Positive
    private Integer numberOfLanes;

    public UpdateSwimmingPoolRequest() {}

    public UpdateSwimmingPoolRequest(String name, Double pricePerHour, Integer capacity, Integer poolLength, Integer numberOfLanes) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.poolLength = poolLength;
        this.numberOfLanes = numberOfLanes;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getPoolLength() { return poolLength; }
    public void setPoolLength(Integer poolLength) { this.poolLength = poolLength; }
    public Integer getNumberOfLanes() { return numberOfLanes; }
    public void setNumberOfLanes(Integer numberOfLanes) { this.numberOfLanes = numberOfLanes; }
}