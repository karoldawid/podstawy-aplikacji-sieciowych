package sfs.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import sfs.model.Rental;
import sfs.rest.dto.CreateRentalRequest;
import sfs.service.RentalException;
import sfs.service.RentalService;

import java.util.List;

@Path("/api/v1/rentals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RentalRestController {

    @Inject
    RentalService rentalService;

    public RentalRestController(RentalService rentalService){
        this.rentalService = rentalService;
    }

    @GET
    @Path("/facility/{facilityId}")
    public List<Rental> getRentalsForFacility(@PathParam("facilityId") String facilityId){
        return rentalService.getRentalsForFacility(facilityId);
    }

    @GET
    @Path("/client/{clientId}")
    public List<Rental> getRentalsForClient(@PathParam("clientId") String clientId){
        return rentalService.getRentalsForClient(clientId);
    }

    @POST
    @Path("/rent")
    public Rental rentFacility(@Valid CreateRentalRequest request) throws RentalException {
        return rentalService.rentFacility(request.getClientId(), request.getFacilityId(), request.getStartTime(), request.getEndTime());
    }

    @GET
    @Path("/client/past/{clientId}")
    public List<Rental> getPastRentalsForClient(@PathParam("clientId") String clientId) {
        return rentalService.getPastRentalsForClient(clientId);
    }

    @GET
    @Path("/client/current/{clientId}")
    public List<Rental> getCurrentRentalsForClient(@PathParam("clientId") String clientId) {
        return rentalService.getCurrentRentalsForClient(clientId);
    }

    @GET
    @Path("/facility/past/{facilityId}")
    public List<Rental> getPastRentalsForFacility(@PathParam("facilityId") String facilityId) {
        return rentalService.getPastRentalsForFacility(facilityId);
    }

    @GET
    @Path("/facility/current/{facilityId}")
    public List<Rental> getCurrentRentalsForFacility(@PathParam("facilityId") String facilityId) {
        return rentalService.getCurrentRentalsForFacility(facilityId);
    }

    @PUT
    @Path("/finish/{id}")
    public Rental endRental(@PathParam("id") String id) throws RentalException {
        return rentalService.endRental(id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteRental(@PathParam("id") String id) throws RentalException {
        rentalService.deleteRental(id);
    }
}
