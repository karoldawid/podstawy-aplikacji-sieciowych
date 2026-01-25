package sfs.rest;

import jakarta.validation.Valid;
import sfs.model.Rental;
import org.springframework.web.bind.annotation.*;
import sfs.rest.dto.CreateRentalRequest;
import sfs.service.RentalException;
import sfs.service.RentalService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalRestController {

    private final RentalService rentalService;

    public RentalRestController(RentalService rentalService){
        this.rentalService = rentalService;
    }

    @GetMapping // To mapuje na GET /api/v1/rentals (zakładając, że klasa ma @RequestMapping("/api/v1/rentals"))
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/facility/{facilityId}")
    public List<Rental> getRentalsForFacility(@PathVariable String facilityId){
        return rentalService.getRentalsForFacility(facilityId);
    }

    @GetMapping("/client/{clientId}")
    public List<Rental> getRentalsForClient(@PathVariable String clientId){
        return rentalService.getRentalsForClient(clientId);
    }

    @PostMapping("/rent")
    public Rental rentFacility(@Valid @RequestBody CreateRentalRequest request) throws RentalException {
        return rentalService.rentFacility(request.getClientId(), request.getFacilityId(), request.getStartTime(), request.getEndTime());
    }

    @GetMapping("/client/past/{clientId}")
    public List<Rental> getPastRentalsForClient(@PathVariable String clientId) {
        return rentalService.getPastRentalsForClient(clientId);
    }

    @GetMapping("/client/current/{clientId}")
    public List<Rental> getCurrentRentalsForClient(@PathVariable String clientId) {
        return rentalService.getCurrentRentalsForClient(clientId);
    }

    @GetMapping("/facility/past/{facilityId}")
    public List<Rental> getPastRentalsForFacility(@PathVariable String facilityId) {
        return rentalService.getPastRentalsForFacility(facilityId);
    }

    @GetMapping("/facility/current/{facilityId}")
    public List<Rental> getCurrentRentalsForFacility(@PathVariable String facilityId) {
        return rentalService.getCurrentRentalsForFacility(facilityId);
    }

    @PutMapping("/finish/{id}")
    public Rental endRental(@PathVariable String id) throws RentalException {
        return rentalService.endRental(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable String id) throws RentalException {
        rentalService.deleteRental(id);
    }
}
