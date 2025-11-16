package sfs.service;

// CRUD
// W metodzie deleteFacility(UUID facilityId)
// musisz wstrzyknąć RentalRepository i sprawdzić,
// czy rentalRepository.findByFacilityId(facilityId)
// jest puste. Jeśli nie jest, musisz rzucić wyjątek
// (nie można usunąć zasobu z rezerwacjami).

import sfs.model.SportsFacility;
import sfs.rest.dto.CreateFacilityRequest;
import sfs.rest.dto.UpdateFacilityRequest;

import java.util.List;
import java.util.UUID;

public interface SportsFacilityService {
    SportsFacility createFacilityFromDTO(CreateFacilityRequest request) throws Exception;
    SportsFacility updateFacility(String id, UpdateFacilityRequest request) throws Exception;
    SportsFacility getFacilityById(String facilityId) throws Exception;
    List<SportsFacility> getAllFacilities();
    void deleteFacility(String facilityId) throws Exception;
}
