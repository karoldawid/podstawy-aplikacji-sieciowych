package sfs.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import sfs.model.User;

import sfs.rest.dto.CreateAdminRequest;
import sfs.rest.dto.CreateClientRequest;
import sfs.rest.dto.CreateFacilityManagerRequest;
import sfs.rest.dto.UpdateUserRequest;
import sfs.service.UserService;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {

    @Inject
    UserService userService;

    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @GET
    @Path("/{id}")
    public User getUserById(@PathParam("id") String id) throws Exception {
        return userService.getUserById(id);
    }

    @GET
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PUT
    @Path("/{id}/activate")
    public User activateUser(@PathParam("id") String id) throws Exception {
        return userService.activateUser(id);
    }

    @PUT
    @Path("/{id}/deactivate")
    public User deactivateUser(@PathParam("id") String id) throws Exception {
        return userService.deactivateUser(id);
    }

    @GET
    @Path("/search/contains")
    public List<User> findUserByLoginFragment(@QueryParam("loginFragment") String loginFragment) throws Exception{
        return userService.findUserByLoginFragment(loginFragment);
    }

    @GET
    @Path("/search/exact")
    public User findUserByLogin(@QueryParam("login") String login) throws Exception {
        return userService.findUserByLogin(login);
    }

    @PUT
    @Path("/{id}")
    public User updateUser(@PathParam("id") String id, @Valid UpdateUserRequest request) throws Exception{
        return userService.updateUser(id, request);
    }

}
