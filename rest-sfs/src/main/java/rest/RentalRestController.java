package rest;

import model.Rental;
import org.springframework.web.bind.annotation.*;
import service.RentalException;
import service.RentalService;

import java.time.LocalDateTime;
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

    @PostMapping("/rentAFacility/{facilityId}/{clientId}/{startTime}/{endTime}")
    public Rental rentFacility(@PathVariable UUID clientId, UUID facilityId , LocalDateTime startTime, LocalDateTime endTime) throws RentalException {
        return rentalService.rentFacility(clientId, facilityId, startTime, endTime);
    }

    // @DeleteMapping dopisać



}
