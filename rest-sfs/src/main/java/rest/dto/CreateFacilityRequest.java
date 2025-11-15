package rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateFacilityRequest {
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
    @Size(min = 3, max = 14)
    private String facilityType;

    private Integer areaInSqm;
    private Boolean hasSauna;
    private String surfaceType;
    private Boolean isIndoor;
    private Integer poolLength;
    private Integer numberOfLanes;

    public CreateFacilityRequest(String name, double pricePerHour, int capacity, String facilityType, Integer areaInSqm, Boolean hasSauna, String surfaceType, Boolean isIndoor, Integer poolLength, Integer numberOfLanes) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.facilityType = facilityType;
        this.areaInSqm = areaInSqm;
        this.hasSauna = hasSauna;
        this.surfaceType = surfaceType;
        this.isIndoor = isIndoor;
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

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public Integer getAreaInSqm() {
        return areaInSqm;
    }

    public void setAreaInSqm(Integer areaInSqm) {
        this.areaInSqm = areaInSqm;
    }

    public Boolean getHasSauna() {
        return hasSauna;
    }

    public void setHasSauna(Boolean hasSauna) {
        this.hasSauna = hasSauna;
    }

    public String getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(String surfaceType) {
        this.surfaceType = surfaceType;
    }

    public Boolean getIndoor() {
        return isIndoor;
    }

    public void setIndoor(Boolean indoor) {
        isIndoor = indoor;
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