package sfs.repository;

import sfs.model.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalRepository {
    Rental save(Rental rental);
    Optional<Rental> findById(String id);
    List<Rental> findAll();
    void deleteById(String id);

    List<Rental> findByClientId(String clientId);
    List<Rental> findByFacilityId(String facilityId);

    List<Rental> findPastByClientId(String clientId, LocalDateTime now);
    List<Rental> findCurrentByClientId(String clientId, LocalDateTime now);
    List<Rental> findPastByFacilityId(String facilityId, LocalDateTime now);
    List<Rental> findCurrentByFacilityId(String facilityId, LocalDateTime now);
}
