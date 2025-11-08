package org.example; // <-- Upewnij się, że jest w tym pakiecie

// Importy są kluczowe!
import org.example.model.Role;
import org.example.model.SportsFacility;
import org.example.model.TennisCourt;
import org.example.model.User;
import org.example.model.SurfaceType;
import org.example.repository.SportsFacilityRepository;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component; // <-- Ten import jest krytyczny

@Component // <-- Ta adnotacja jest krytyczna
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SportsFacilityRepository sportsFacilityRepository;

    // Spring automatycznie wstrzyknie tu Twoje repozytoria
    public DataLoader(UserRepository userRepository, SportsFacilityRepository sportsFacilityRepository) {
        this.userRepository = userRepository;
        this.sportsFacilityRepository = sportsFacilityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Tworzymy użytkowników
        User client1 = new User("Jan", "Kowalski", "client1", Role.CLIENT);
        User admin = new User("Admin", "Admin", "admin", Role.ADMIN);
        User manager = new User("Marek", "Nowak", "manager1", Role.MANAGER);

        userRepository.save(client1);
        userRepository.save(admin);
        userRepository.save(manager);

        // Tworzymy zasoby
        SportsFacility court1 = new TennisCourt("Kort Centralny", 120.0, 4, SurfaceType.CLAY, true);
        SportsFacility court2 = new TennisCourt("Kort Boczny", 80.0, 4, SurfaceType.HARD, false);

        sportsFacilityRepository.save(court1);
        sportsFacilityRepository.save(court2);

        System.out.println("====== DANE TESTOWE ZAŁADOWANE ======");
    }
}