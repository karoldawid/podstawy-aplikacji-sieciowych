package sfs.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import sfs.model.User;
import sfs.rest.dto.CreateAdminRequest;
import sfs.service.UserService;

@Path("/api/v1/admins")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminRestController {

    @Inject
    UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @POST
    public RestResponse<User> createAdmin(@Valid CreateAdminRequest request) throws Exception {
        User admin = userService.createAdmin(request);
        return RestResponse.status(RestResponse.Status.CREATED, admin);
    }
}
