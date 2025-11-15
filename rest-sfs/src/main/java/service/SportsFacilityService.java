package service;

// CRUD
// W metodzie deleteFacility(UUID facilityId)
// musisz wstrzyknąć RentalRepository i sprawdzić,
// czy rentalRepository.findByFacilityId(facilityId)
// jest puste. Jeśli nie jest, musisz rzucić wyjątek
// (nie można usunąć zasobu z rezerwacjami).

import model.SportsFacility;
import rest.dto.CreateFacilityRequest;
import rest.dto.UpdateFacilityRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SportsFacilityService {
    SportsFacility createFacilityFromDTO(CreateFacilityRequest request) throws Exception;
    SportsFacility updateFacility(UUID id, UpdateFacilityRequest request) throws Exception;
    SportsFacility getFacilityById(UUID facilityId) throws Exception;
    List<SportsFacility> getAllFacilities();
    void deleteFacility(UUID facilityId) throws Exception;
}
