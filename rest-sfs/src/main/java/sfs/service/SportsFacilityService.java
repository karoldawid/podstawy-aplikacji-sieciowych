package sfs.service;

import sfs.model.Gym;
import sfs.model.SportsFacility;

import sfs.model.SwimmingPool;
import sfs.model.TennisCourt;
import sfs.rest.dto.*;

import java.util.List;


public interface SportsFacilityService {
    SportsFacility createGym(CreateGymRequest request) throws Exception;
    SportsFacility createTennisCourt(CreateTennisCourtRequest request) throws Exception;
    SportsFacility createSwimmingPool(CreateSwimmingPoolRequest request) throws Exception;

    Gym updateGym(String id, UpdateGymRequest request) throws Exception;
    TennisCourt updateTennisCourt(String id, UpdateTennisCourtRequest request) throws Exception;
    SwimmingPool updateSwimmingPool(String id, UpdateSwimmingPoolRequest request) throws Exception;

    SportsFacility getFacilityById(String facilityId) throws Exception;
    List<SportsFacility> getAllFacilities();
    void deleteFacility(String facilityId) throws Exception;
}
