package sfs.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpdateTennisCourtRequest {

    @NotBlank @Size(min = 3, max = 20)
    private String name;

    @NotNull @Positive
    private Double pricePerHour;

    @NotNull @Positive
    private Integer capacity;

    @NotBlank
    private String surfaceType;

    @NotNull
    @JsonProperty("isIndoor")
    private Boolean isIndoor;

    public UpdateTennisCourtRequest() {}

    public UpdateTennisCourtRequest(String name, Double pricePerHour, Integer capacity, String surfaceType, Boolean isIndoor) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
        this.surfaceType = surfaceType;
        this.isIndoor = isIndoor;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getSurfaceType() { return surfaceType; }
    public void setSurfaceType(String surfaceType) { this.surfaceType = surfaceType; }
    public Boolean getIsIndoor() { return isIndoor; }
    public void setIsIndoor(Boolean isIndoor) { this.isIndoor = isIndoor; }
}