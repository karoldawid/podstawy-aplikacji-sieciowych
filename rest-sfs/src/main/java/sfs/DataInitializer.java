package sfs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.stereotype.Component;
import sfs.model.SportsFacility;
import sfs.model.User;
import sfs.rest.dto.*;
import sfs.service.RentalService;
import sfs.service.SportsFacilityService;
import sfs.service.UserService;

import java.time.LocalDateTime;

@Profile("!test")
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final SportsFacilityService sportsFacilityService;
    private final RentalService rentalService;
    private final MongoTemplate mongoTemplate;

    public DataInitializer(UserService userService,
                           SportsFacilityService sportsFacilityService,
                           RentalService rentalService,
                           MongoTemplate mongoTemplate) {
        this.userService = userService;
        this.sportsFacilityService = sportsFacilityService;
        this.rentalService = rentalService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {

        mongoTemplate.dropCollection("users");
        mongoTemplate.dropCollection("facilities");
        mongoTemplate.dropCollection("rentals");

        MongoJsonSchema userSchema = MongoJsonSchema.builder()
                .required("login", "firstName", "lastName")
                .properties(
                        JsonSchemaProperty.string("login").minLength(4).maxLength(20),
                        JsonSchemaProperty.string("firstName").minLength(3).maxLength(20),
                        JsonSchemaProperty.string("lastName").minLength(4).maxLength(20)
                )
                .build();
        mongoTemplate.createCollection("users", CollectionOptions.empty().schema(userSchema));

        mongoTemplate.indexOps("users").createIndex(
                new Index().on("login", Sort.Direction.ASC).unique()
        );

        MongoJsonSchema facilitySchema = MongoJsonSchema.builder()
                .required("name", "pricePerHour", "capacity")
                .properties(
                        JsonSchemaProperty.string("name").minLength(3).maxLength(20),
                        JsonSchemaProperty.float64("pricePerHour").gte(0.0),
                        JsonSchemaProperty.int32("capacity").gte(1)
                )
                .build();
        mongoTemplate.createCollection("facilities", CollectionOptions.empty().schema(facilitySchema));

        MongoJsonSchema rentalSchema = MongoJsonSchema.builder()
                .required("clientId", "facilityId", "startTime", "endTime")
                .properties(
                        JsonSchemaProperty.string("clientId").minLength(1),
                        JsonSchemaProperty.string("facilityId").minLength(1),
                        JsonSchemaProperty.date("startTime"),
                        JsonSchemaProperty.date("endTime")
                )
                .build();
        mongoTemplate.createCollection("rentals", CollectionOptions.empty().schema(rentalSchema));


        SportsFacility gym1 = sportsFacilityService.createGym(
                new CreateGymRequest(
                        "FitFabric",
                        50.0,
                        200,
                        50,
                        true
                )
        );

        SportsFacility court1 = sportsFacilityService.createTennisCourt(
                new CreateTennisCourtRequest(
                        "Korty \"Szybka Piłka\"",
                        80.0,
                        4,
                        "CLAY",
                        true
                )
        );

        SportsFacility pool1 = sportsFacilityService.createSwimmingPool(
                new CreateSwimmingPoolRequest(
                        "Pływalnia \"Fala\"",
                        30.0,
                        100,
                        25,
                        6
                )
        );

        User admin = userService.createAdmin(
                new CreateAdminRequest("kdawid", "Karol", "Dawid")
        );

        User manager1 = userService.createFacilityManager(
                new CreateFacilityManagerRequest("jkowalski", "Jan", "Kowalski")
        );

        User client1 = userService.createClient(
                new CreateClientRequest("mchodulski", "Mateusz", "Chodulski")
        );

        User client2 = userService.createClient(
                new CreateClientRequest("anowak", "Anna", "Nowak")
        );

        userService.activateUser(client1.getId());
        userService.activateUser(client2.getId());


        try {
            rentalService.rentFacility(
                    client1.getId(),
                    court1.getId(),
                    LocalDateTime.now().plusDays(2).withHour(10).withMinute(0),
                    LocalDateTime.now().plusDays(2).withHour(11).withMinute(0)
            );

            rentalService.rentFacility(
                    client2.getId(),
                    pool1.getId(),
                    LocalDateTime.now().plusDays(3).withHour(18).withMinute(0),
                    LocalDateTime.now().plusDays(3).withHour(19).withMinute(0)
            );

        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia przykładowych rezerwacji: " + e.getMessage());
        }
    }
}