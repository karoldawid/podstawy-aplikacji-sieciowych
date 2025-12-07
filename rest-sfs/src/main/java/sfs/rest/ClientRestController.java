package sfs.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import sfs.model.User;
import sfs.rest.dto.CreateClientRequest;
import sfs.service.UserService;

@Path("/api/v1/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientRestController {

    @Inject
    UserService userService;

    public ClientRestController(UserService userService) {
        this.userService = userService;
    }

    @POST
    public RestResponse<User> createClient(@Valid CreateClientRequest request) throws Exception {
        User client = userService.createClient(request);
        return RestResponse.status(RestResponse.Status.CREATED, client);
    }
}
