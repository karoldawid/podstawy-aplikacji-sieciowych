package sfs.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateGymRequest {

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
    @Positive
    private Integer areaInSqm;

    @NotNull
    private Boolean hasSauna;

    public CreateGymRequest() {
    }

    public CreateGymRequest(String name, double pricePerHour, int capacity, Integer areaInSqm, Boolean hasSauna) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.areaInSqm = areaInSqm;
        this.hasSauna = hasSauna;
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
}
