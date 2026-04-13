package sfs.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import sfs.exception.ResourceConflictException;
import sfs.exception.ResourceNotFoundException;
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
    public Rental rentFacility(String clientId, String facilityId, LocalDateTime startTime, LocalDateTime endTime) throws RentalException {
        // dla admina, facility managera i tylko oni moga  zniej korzystac
        return createRentalLogic(clientId, facilityId, startTime, endTime);
    }

    @Override
    public Rental rentFacilitySelf(String facilityId, LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        // k: wyciagamy login z contextu i stad wiemy na kto wypozycza
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Zalogowany użytkownik nie istnieje w bazie."));

        if (!(user instanceof Client)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tylko Klienci mogą dokonywać tej operacji.");
        }

        return createRentalLogic(user.getId(), facilityId, startTime, endTime);
    }

    private Rental createRentalLogic(String clientId, String facilityId, LocalDateTime startTime, LocalDateTime endTime) throws RentalException {

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new RentalException("Czas rozpoczęcia wypożyczenia obiektu musi być przed czasem zakończenia");
        }

        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik o ID: " + clientId + " nie istnieje."));

        if (!(user instanceof Client)){
            throw new RentalException("Użytkownik o ID: " + clientId + " nie jest klientem (nie można utworzyć rezerwacji).");
        }

        if (!user.isActive()){
            throw new RentalException("Klient o ID: " + clientId + " nie jest aktywny.");
        }

        SportsFacility sportsFacility = sportsFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Obiekt sportowy o ID: " + facilityId + " nie istnieje."));

        synchronized (rentalLock) {
            if (!isFacilityAvailable(facilityId, startTime, endTime)) {
                throw new ResourceConflictException("Obiekt sportowy o ID: " + facilityId + " jest już wypożyczony w tym terminie.");
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
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    public Rental endRental(String id) throws RentalException {
        Rental rental =  rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono rezerwacji o ID: " + id));
        rental.setEndTime(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    @Override
    public List<Rental> getMyRentals() { // Usunąłem "throws Exception", bo ResourceNotFoundException to RuntimeException
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd spójności danych: Zalogowany użytkownik '" + login + "' nie istnieje w bazie."));

        return rentalRepository.findByClientId(user.getId());
    }

    // usuwanie alokacji dotyczy tylko alokacji nie zakończonych
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
