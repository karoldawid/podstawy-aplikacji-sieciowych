package sfs.service;

import sfs.model.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RentalService {
    Rental rentFacility(String clientId, String facilityId, LocalDateTime startTime, LocalDateTime endTime)
            throws RentalException;
    boolean isFacilityAvailable(String facilityId, LocalDateTime startTime, LocalDateTime endTime);

    List<Rental> getRentalsForFacility(String facilityId);
    List<Rental> getRentalsForClient(String clientId);
    List<Rental> getPastRentalsForClient(String clientId);
    List<Rental> getCurrentRentalsForClient(String clientId);
    List<Rental> getPastRentalsForFacility(String facilityId);
    List<Rental> getCurrentRentalsForFacility(String facilityId);

    Rental endRental(String id) throws RentalException;
    void deleteRental(String id) throws RentalException;
}
