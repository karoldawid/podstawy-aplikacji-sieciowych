package sfs.rest;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import sfs.model.Gym;
import sfs.model.SportsFacility;
import sfs.model.SwimmingPool;
import sfs.model.TennisCourt;
import sfs.rest.dto.*;
import sfs.service.SportsFacilityService;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/facilities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SportsFacilityRestController {

    @Inject
    SportsFacilityService sportsFacilityService;

    public SportsFacilityRestController(SportsFacilityService sportsFacilityService) {
        this.sportsFacilityService = sportsFacilityService;
    }

    @GET
    @Path("/{facilityId}")
    public SportsFacility getFacilityById(@PathParam("facilityId") String facilityId) throws Exception {
        return sportsFacilityService.getFacilityById(facilityId);
    }

    @GET
    public List<SportsFacility> getAllFacilities() {
        return sportsFacilityService.getAllFacilities();
    }

    @POST
    @Path("/tennis-courts")
    public SportsFacility createTennisCourt(@Valid CreateTennisCourtRequest request) throws Exception {
        return sportsFacilityService.createTennisCourt(request);
    }

    @POST
    @Path("/gyms")
    public SportsFacility createGym(@Valid CreateGymRequest request) throws Exception {
        return  sportsFacilityService.createGym(request);
    }

    @POST
    @Path("/swimming-pools")
    public SportsFacility createSwimmingPool(@Valid CreateSwimmingPoolRequest request) throws Exception {
        return  sportsFacilityService.createSwimmingPool(request);
    }

    @PUT
    @Path("/gyms/{id}")
    public Gym updateGym(@PathParam("id") String id, @Valid UpdateGymRequest request) throws Exception {
        return sportsFacilityService.updateGym(id, request);
    }

    @PUT
    @Path("/tennis-courts/{id}")
    public TennisCourt updateTennisCourt(@PathParam("id") String id, @Valid UpdateTennisCourtRequest request) throws Exception {
        return sportsFacilityService.updateTennisCourt(id, request);
    }

    @PUT
    @Path("/swimming-pools/{id}")
    public SwimmingPool updateSwimmingPool(@PathParam("id") String id, @Valid UpdateSwimmingPoolRequest request) throws Exception {
        return sportsFacilityService.updateSwimmingPool(id, request);
    }

    @DELETE
    @Path("/{facilityId}")
    public void deleteFacility(@PathParam("facilityId") String facilityId) throws Exception {
        sportsFacilityService.deleteFacility(facilityId);
    }

}




