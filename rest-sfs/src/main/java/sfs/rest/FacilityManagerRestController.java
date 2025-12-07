package sfs.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import sfs.model.User;
import sfs.rest.dto.CreateFacilityManagerRequest;
import sfs.service.UserService;

@Path("/api/v1/facility-managers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacilityManagerRestController {

    @Inject
    UserService userService;

    public FacilityManagerRestController(UserService userService) {
        this.userService = userService;
    }

    @POST
    public RestResponse<User> createFacilityManager(@Valid CreateFacilityManagerRequest request) throws Exception {
        User manager = userService.createFacilityManager(request);
        return RestResponse.status(RestResponse.Status.CREATED, manager);
    }
}
