package sfs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sfs.rest.dto.CreateFacilityRequest;
import sfs.rest.dto.CreateUserRequest;
import sfs.service.SportsFacilityService;
import sfs.service.UserService;

@Profile("!test") // DataInitializer uruchamia się w momencie, nieaktywny jest profil 'test' w Tests
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final SportsFacilityService sportsFacilityService;

    public DataInitializer(UserService userService, SportsFacilityService sportsFacilityService) {
        this.userService = userService;
        this.sportsFacilityService = sportsFacilityService;
    }

    @Override
    public void run(String... args) throws Exception {
        sportsFacilityService.createFacilityFromDTO(
                new CreateFacilityRequest(
                        "FitFabric",
                        50,
                        200,
                        "GYM",
                        50,
                        true,
                        null,
                        null,
                        null,
                        null));

        userService.createUserFromDTO(new CreateUserRequest("kdawid", "Karol", "Dawid", "ADMIN"));
        userService.createUserFromDTO(new CreateUserRequest("mchodulski", "Mateusz", "Chodulski", "CLIENT"));

        // uruchamianie aplikacji powinno wiązać się z wczytaniem zestawu danych inicjujących
        // TODO: dopisać coś jeszcze
    }
}
