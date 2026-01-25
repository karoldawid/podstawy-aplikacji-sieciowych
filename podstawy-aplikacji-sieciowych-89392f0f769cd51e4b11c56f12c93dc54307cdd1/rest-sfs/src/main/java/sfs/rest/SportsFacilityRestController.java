package sfs.rest;

import jakarta.validation.Valid;
import sfs.model.Gym;
import sfs.model.SportsFacility;
import org.springframework.web.bind.annotation.*;
import sfs.model.SwimmingPool;
import sfs.model.TennisCourt;
import sfs.rest.dto.*;
import sfs.service.SportsFacilityService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/facilities")
public class SportsFacilityRestController {

    private final SportsFacilityService sportsFacilityService;

    public SportsFacilityRestController(SportsFacilityService sportsFacilityService) {
        this.sportsFacilityService = sportsFacilityService;
    }

    @GetMapping("/{facilityId}")
    public SportsFacility getFacilityById(@PathVariable String facilityId) throws Exception {
        return sportsFacilityService.getFacilityById(facilityId);
    }

    @GetMapping()
    public List<SportsFacility> getAllFacilities() {
        return sportsFacilityService.getAllFacilities();
    }

    @PostMapping("/tennis-courts")
    public SportsFacility createTennisCourt(@Valid @RequestBody CreateTennisCourtRequest request) throws Exception {
        return sportsFacilityService.createTennisCourt(request);
    }

    @PostMapping("/gyms")
    public SportsFacility createGym(@Valid @RequestBody CreateGymRequest request) throws Exception {
        return  sportsFacilityService.createGym(request);
    }

    @PostMapping("/swimming-pools")
    public SportsFacility createSwimmingPool(@Valid @RequestBody CreateSwimmingPoolRequest request) throws Exception {
        return  sportsFacilityService.createSwimmingPool(request);
    }

    @PutMapping("/gyms/{id}")
    public Gym updateGym(@PathVariable String id, @Valid @RequestBody UpdateGymRequest request) throws Exception {
        return sportsFacilityService.updateGym(id, request);
    }

    @PutMapping("/tennis-courts/{id}")
    public TennisCourt updateTennisCourt(@PathVariable String id, @Valid @RequestBody UpdateTennisCourtRequest request) throws Exception {
        return sportsFacilityService.updateTennisCourt(id, request);
    }

    @PutMapping("/swimming-pools/{id}")
    public SwimmingPool updateSwimmingPool(@PathVariable String id, @Valid @RequestBody UpdateSwimmingPoolRequest request) throws Exception {
        return sportsFacilityService.updateSwimmingPool(id, request);
    }

//    @PutMapping("/{facilityId}")
//    public SportsFacility updateFacility(@PathVariable String facilityId, @Valid @RequestBody UpdateFacilityRequest request) throws Exception {
//        return sportsFacilityService.updateFacility(facilityId, request);
//    }

    @DeleteMapping("/{facilityId}")
    public void deleteFacility(@PathVariable String facilityId) throws Exception {
        sportsFacilityService.deleteFacility(facilityId);
    }

}




