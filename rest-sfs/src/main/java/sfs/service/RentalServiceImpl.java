package sfs.service;

import sfs.model.Client;
import sfs.model.User;
import sfs.model.Rental;
import sfs.model.SportsFacility;
import org.springframework.stereotype.Service;
import sfs.repository.UserRepository;
import sfs.repository.RentalRepository;
import sfs.repository.SportsFacilityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService{
    private final UserRepository userRepository;
    private final SportsFacilityRepository sportsFacilityRepository;
    private final RentalRepository rentalRepository;

    private final Object rentalLock = new Object();

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

        User user = userRepository.findById(clientId).orElseThrow(() -> new RentalException("Użytkownik o ID: " + clientId + " nie istnieje."));

        if (!(user instanceof Client)){
            throw new RentalException("Użytkownik o ID: " + clientId + " nie jest klientem.");
        }

        if (!user.isActive()){
            throw new RentalException("Klient o ID: " + clientId + " nie jest aktywny.");
        }

        SportsFacility sportsFacility = sportsFacilityRepository.findById(facilityId).orElseThrow(() -> new RentalException("Obiekt sportowy o ID: " + facilityId + " nie istnieje."));

        // na PAS trzeba sprawdzić czy klient jest aktywny
        synchronized (rentalLock) {
            if (!isFacilityAvailable(facilityId, startTime, endTime)) {
                throw new RentalException("Obiekt sportowy o ID: " + facilityId + " jest juz wypozyczony.");
                // metoda poniżej to sprawdza
            }

            Rental newRental = new Rental(clientId, facilityId, startTime, endTime);
            return rentalRepository.save(newRental);
        }
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
    public List<Rental> getRentalsForClient(UUID clientId) {
        return rentalRepository.findByClientId(clientId);
    }

    @Override
    public List<Rental> getRentalsForFacility(UUID facilityId) {
        return rentalRepository.findByFacilityId(facilityId);
    }

    @Override
    public List<Rental> getPastRentalsForClient(UUID clientId) {
         return rentalRepository.findByClientId(clientId)
                 .stream()
                 .filter(rental -> rental.getEndTime() != null && rental.getEndTime().isBefore(LocalDateTime.now()))
                 .collect(Collectors.toList());
    }

    @Override
    public List<Rental> getCurrentRentalsForClient(UUID clientId) {
        return rentalRepository.findByClientId(clientId)
                .stream()
                .filter(rental -> rental.getEndTime() == null || rental.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> getPastRentalsForFacility(UUID facilityId) {
        return rentalRepository.findByFacilityId(facilityId)
                .stream()
                .filter(rental -> rental.getEndTime() != null && rental.getEndTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> getCurrentRentalsForFacility(UUID facilityId) {
        return rentalRepository.findByFacilityId(facilityId)
                .stream()
                .filter(rental -> rental.getEndTime() == null || rental.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    // zakończenie alokacji polega na ustawieniu atrybutu czasu zakończenia alokacji
    @Override
    public Rental endRental(UUID id) throws RentalException {
        Rental rental =  rentalRepository.findById(id).orElseThrow(() -> new RentalException("Nie znaleziono rezerwacji o ID: " + id));
        rental.setEndTime(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    // usuwanie alokacji dotyczy tylko alokacji nie zakończonych
    @Override
    public void deleteRental(UUID id) throws RentalException {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new RentalException("Wypożyczenie o ID: " + id + " nie istnieje."));

        if (rental.getEndTime() != null && rental.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RentalException("Nie można usunąć rezerwacji o ID: " + id + ", ponieważ jest to rezerwacja zakończona.");
        } else {
            rentalRepository.deleteById(id);
        }
    }
}
