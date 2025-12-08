package sfs.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sfs.exception.ResourceConflictException;
import sfs.exception.ResourceNotFoundException;
import sfs.model.Client;
import sfs.model.User;
import sfs.model.Rental;
import sfs.model.SportsFacility;
import sfs.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RentalServiceImpl implements RentalService{

    private final MongoUserRepository userRepository;
    private final MongoSportsFacilityRepository sportsFacilityRepository;
    private final MongoRentalRepository rentalRepository;

    private final Object rentalLock = new Object();

    @Inject
    public RentalServiceImpl(MongoUserRepository userRepository, MongoSportsFacilityRepository sportsFacilityRepository, MongoRentalRepository rentalRepository){
        this.userRepository = userRepository;
        this.sportsFacilityRepository = sportsFacilityRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Rental rentFacility(String clientId, String facilityId, LocalDateTime startTime, LocalDateTime endTime) throws RentalException {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new RentalException("Czas rozpoczęcia wypożyczenia obiektu musi być przed czasem zakończenia");
        }

        User user = userRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Użytkownik o ID: " + clientId + " nie istnieje."));

        if (!(user instanceof Client)){
            throw new RentalException("Użytkownik o ID: " + clientId + " nie jest klientem.");
        }

        if (!user.isActive()){
            throw new RentalException("Klient o ID: " + clientId + " nie jest aktywny.");
        }

        SportsFacility sportsFacility = sportsFacilityRepository.findById(facilityId).orElseThrow(() -> new ResourceNotFoundException("Obiekt sportowy o ID: " + facilityId + " nie istnieje."));

        synchronized (rentalLock) {
            if (!isFacilityAvailable(facilityId, startTime, endTime)) {
                throw new ResourceConflictException("Obiekt sportowy o ID: " + facilityId + " jest juz wypozyczony.");
            }

            Rental newRental = new Rental(clientId, facilityId, startTime, endTime);
            return rentalRepository.save(newRental);
        }
    }

    @Override
    public boolean isFacilityAvailable(String facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Rental> existingRentals = rentalRepository.findByFacilityId(facilityId);

        for(Rental existing : existingRentals){
            if(existing.overlaps(startTime, endTime)){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Rental> getRentalsForClient(String clientId) {
        return rentalRepository.findByClientId(clientId);
    }

    @Override
    public List<Rental> getRentalsForFacility(String facilityId) {
        return rentalRepository.findByFacilityId(facilityId);
    }

    @Override
    public List<Rental> getPastRentalsForClient(String clientId) {
        return rentalRepository.findPastByClientId(clientId, LocalDateTime.now());
    }

    @Override
    public List<Rental> getCurrentRentalsForClient(String clientId) {
        return rentalRepository.findCurrentByClientId(clientId, LocalDateTime.now());
    }

    @Override
    public List<Rental> getPastRentalsForFacility(String facilityId) {
        return rentalRepository.findPastByFacilityId(facilityId, LocalDateTime.now());
    }

    @Override
    public List<Rental> getCurrentRentalsForFacility(String facilityId) {
        return rentalRepository.findCurrentByFacilityId(facilityId, LocalDateTime.now());
    }

    @Override
    public Rental endRental(String id) throws RentalException {
        Rental rental =  rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono rezerwacji o ID: " + id));
        rental.setEndTime(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    @Override
    public void deleteRental(String id) throws RentalException {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Wypożyczenie o ID: " + id + " nie istnieje."));

        if (rental.getEndTime() != null && rental.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RentalException("Nie można usunąć rezerwacji o ID: " + id + ", ponieważ jest to rezerwacja zakończona.");
        } else {
            rentalRepository.deleteById(id);
        }
    }
}
