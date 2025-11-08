package org.example.service;

import org.example.model.Role;
import org.example.model.User;
import org.example.model.Rental;
import org.example.model.SportsFacility;
import org.example.repository.UserRepository;
import org.example.repository.RentalRepository;
import org.example.repository.SportsFacilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService{
    private final UserRepository userRepository;
    private final SportsFacilityRepository sportsFacilityRepository;
    private final RentalRepository rentalRepository;

    public RentalServiceImpl(UserRepository userRepository, SportsFacilityRepository sportsFacilityRepository, RentalRepository rentalRepository){
        this.userRepository = userRepository;
        this.sportsFacilityRepository = sportsFacilityRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Rental rentFacility(UUID clientId, UUID facilityId, LocalDateTime startTime, LocalDateTime endTime) throws RentalException {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new RentalException("Czas rozpoczęcia wypożyczenia obiektu musi być przed czasem zakończenia");
        }
        if (startTime.isBefore(LocalDateTime.now())){
            throw new RentalException("Nie da się wypożyczyć wstecz");
        }

        User user = userRepository.findById(clientId).orElseThrow(() -> new RentalException("Klient o ID: " + clientId + " nie istnieje."));
        SportsFacility sportsFacility = sportsFacilityRepository.findById(facilityId).orElseThrow(() -> new RentalException("Obiekt sportowy o ID: " + facilityId + " nie istnieje."));

        if (user.getRole() != Role.CLIENT) {
            throw new RentalException("Tylko użytkownik z rolą KLIENT może dokonywać rezerwacji.");
        }
        if (!user.isActive()) {
            throw new RentalException("Użytkownik jest nieaktywny i nie może dokonywać rezerwacji.");
        }

        if (!isFacilityAvailable(facilityId, startTime, endTime)){
            throw new RentalException("Obiekt sportowy o ID: " + facilityId + " jest juz wypozyczony.");
            // metoda poniżej to sprawdza
        }

        Rental newRental = new Rental(clientId, facilityId, startTime, endTime);
        return rentalRepository.save(newRental);
    }

    @Override
    public boolean isFacilityAvailable(UUID facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Rental> existingRentals = rentalRepository.findByFacilityId(facilityId);

        for(Rental existing : existingRentals){
            if(existing.overlaps(startTime, endTime)){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Rental> getRentalsForUser(UUID clientId) {
        return rentalRepository.findByUserId(clientId);
    }

    @Override
    public List<Rental> getRentalsForFacility(UUID facilityId) {
        return rentalRepository.findByFacilityId(facilityId);
    }
}
