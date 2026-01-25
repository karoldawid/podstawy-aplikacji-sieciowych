package sfs.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sfs.model.User;
import sfs.rest.dto.CreateClientRequest;
import sfs.service.UserService;

@RestController
@RequestMapping("api/v1/clients")
public class ClientRestController {
    private final UserService userService;
    public ClientRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createClient(@Valid @RequestBody CreateClientRequest request) throws Exception {
        return userService.createClient(request);
    }
}
