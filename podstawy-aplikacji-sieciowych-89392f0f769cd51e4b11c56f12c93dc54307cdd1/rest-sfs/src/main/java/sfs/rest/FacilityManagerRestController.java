package sfs.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sfs.model.User;
import sfs.rest.dto.CreateFacilityManagerRequest;
import sfs.service.UserService;

@RestController
@RequestMapping("/api/v1/facility-managers")
public class FacilityManagerRestController {

    private final UserService userService;

    public FacilityManagerRestController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createFacilityManager(@Valid @RequestBody CreateFacilityManagerRequest request) throws Exception {
        return userService.createFacilityManager(request);
    }
}
