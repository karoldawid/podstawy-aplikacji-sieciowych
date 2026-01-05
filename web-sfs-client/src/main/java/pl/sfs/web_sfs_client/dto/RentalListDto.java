package pl.sfs.web_sfs_client.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalListDto {
    private String id;
    private String facilityId;
    private String clientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
