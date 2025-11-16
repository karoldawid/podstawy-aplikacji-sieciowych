package sfs.service;

import sfs.model.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RentalService {
    Rental rentFacility(UUID clientId, UUID facilityId, LocalDateTime startTime, LocalDateTime endTime)
            throws RentalException;
    boolean isFacilityAvailable(UUID facilityId, LocalDateTime startTime, LocalDateTime endTime);

    List<Rental> getRentalsForFacility(UUID facilityId);
    List<Rental> getRentalsForClient(UUID clientId);
    List<Rental> getPastRentalsForClient(UUID clientId);
    List<Rental> getCurrentRentalsForClient(UUID clientId);
    List<Rental> getPastRentalsForFacility(UUID facilityId);
    List<Rental> getCurrentRentalsForFacility(UUID facilityId);

    Rental endRental(UUID id) throws RentalException;
    void deleteRental(UUID id) throws RentalException;
}
