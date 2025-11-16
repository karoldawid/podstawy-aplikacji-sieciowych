package sfs.rest;

import jakarta.validation.Valid;
import sfs.model.Rental;
import org.springframework.web.bind.annotation.*;
import sfs.rest.dto.CreateRentalRequest;
import sfs.service.RentalException;
import sfs.service.RentalService;

import java.util.List;
import java.util.UUID;

@RestController // obsługa HTTP, return JSON
@RequestMapping("/api/v1/rentals")
public class RentalRestController {

    private final RentalService rentalService;

    public RentalRestController(RentalService rentalService){
        this.rentalService = rentalService;
    }

    // GET http://localhost:8080/api/v1/rentals/facility/{jakiś-UUID}
    @GetMapping("/facility/{facilityId}")
    public List<Rental> getRentalsForFacility(@PathVariable UUID facilityId){
        return rentalService.getRentalsForFacility(facilityId);
    }

    // GET http://localhost:8080/api/v1/rentals/clients/{UUID}
    @GetMapping("/client/{clientId}")
    public List<Rental> getRentalsForClient(@PathVariable UUID clientId){
        return rentalService.getRentalsForClient(clientId);
    }

    @PostMapping("/rent")
    public Rental rentFacility(@Valid @RequestBody CreateRentalRequest request) throws RentalException {
        return rentalService.rentFacility(request.getClientId(), request.getFacilityId(), request.getStartTime(), request.getEndTime());
    }

    @GetMapping("/client/past/{clientId}")
    public List<Rental> getPastRentalsForClient(@PathVariable UUID clientId) {
        return rentalService.getPastRentalsForClient(clientId);
    }

    @GetMapping("/client/current/{clientId}")
    public List<Rental> getCurrentRentalsForClient(@PathVariable UUID clientId) {
        return rentalService.getCurrentRentalsForClient(clientId);
    }

    @GetMapping("/facility/past/{facilityId}")
    public List<Rental> getPastRentalsForFacility(@PathVariable UUID facilityId) {
        return rentalService.getPastRentalsForFacility(facilityId);
    }

    @GetMapping("/facility/current/{facilityId}")
    public List<Rental> getCurrentRentalsForFacility(@PathVariable UUID facilityId) {
        return rentalService.getCurrentRentalsForFacility(facilityId);
    }

    @PutMapping("/finish/{id}")
    public Rental endRental(@PathVariable UUID id) throws RentalException {
        return rentalService.endRental(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable UUID id) throws RentalException {
        rentalService.deleteRental(id);
    }
}
