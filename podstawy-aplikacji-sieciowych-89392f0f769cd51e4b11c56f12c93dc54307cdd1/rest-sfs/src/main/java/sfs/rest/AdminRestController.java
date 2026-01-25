package sfs.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sfs.model.User;
import sfs.rest.dto.CreateAdminRequest;
import sfs.service.UserService;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminRestController {
    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createAdmin(@Valid @RequestBody CreateAdminRequest request) throws Exception {
        return userService.createAdmin(request);
    }
}
