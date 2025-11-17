package sfs.service;

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
