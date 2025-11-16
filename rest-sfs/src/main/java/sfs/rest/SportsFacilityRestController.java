package sfs.rest;

import jakarta.validation.Valid;
import sfs.model.SportsFacility;
import org.springframework.web.bind.annotation.*;
import sfs.rest.dto.CreateFacilityRequest;
import sfs.rest.dto.UpdateFacilityRequest;
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

    @PostMapping("/create")
    public SportsFacility createFacility(@Valid @RequestBody CreateFacilityRequest request) throws Exception {
        return sportsFacilityService.createFacilityFromDTO(request);
    }

    @PutMapping("/{facilityId}")
    public SportsFacility updateFacility(@PathVariable String facilityId, @Valid @RequestBody UpdateFacilityRequest request) throws Exception {
        return sportsFacilityService.updateFacility(facilityId, request);
    }

    @DeleteMapping("/{facilityId}")
    public void deleteFacility(@PathVariable String facilityId) throws Exception {
        sportsFacilityService.deleteFacility(facilityId);
    }

}




