package sfs.rest.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateRentalRequest {
    @NotNull
    private String facilityId;
    @NotNull
    private String clientId;
    @NotNull
    @FutureOrPresent
    private LocalDateTime startTime;
    @NotNull
    @Future
    private LocalDateTime endTime;

    public CreateRentalRequest() {
    }

    public CreateRentalRequest(String facilityId, String clientId, LocalDateTime startTime, LocalDateTime endTime) {
        this.facilityId = facilityId;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    //    public UUID getFacilityId() {
//        return facilityId;
//    }
//
//    public void setFacilityId(UUID facilityId) {
//        this.facilityId = facilityId;
//    }
//
//    public UUID getClientId() {
//        return clientId;
//    }
//
//    public void setClientId(UUID clientId) {
//        this.clientId = clientId;
//    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
