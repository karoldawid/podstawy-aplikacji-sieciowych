package sfs.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpdateGymRequest {

    @NotBlank @Size(min = 3, max = 20)
    private String name;

    @NotNull @Positive
    private Double pricePerHour;

    @NotNull @Positive
    private Integer capacity;

    @NotNull @Positive
    private Integer areaInSqm;

    @NotNull
    @JsonProperty("hasSauna")
    private Boolean hasSauna;

    public UpdateGymRequest() {}

    public UpdateGymRequest(String name, Double pricePerHour, Integer capacity, Integer areaInSqm, Boolean hasSauna) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.areaInSqm = areaInSqm;
        this.hasSauna = hasSauna;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getAreaInSqm() { return areaInSqm; }
    public void setAreaInSqm(Integer areaInSqm) { this.areaInSqm = areaInSqm; }
    public Boolean getHasSauna() { return hasSauna; }
    public void setHasSauna(Boolean hasSauna) { this.hasSauna = hasSauna; }
}