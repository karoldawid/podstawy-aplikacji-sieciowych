package sfs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ValidationOptions;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.bson.Document;
import sfs.model.SportsFacility;
import sfs.model.User;
import sfs.rest.dto.*;
import sfs.service.RentalService;
import sfs.service.SportsFacilityService;
import sfs.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;

@ApplicationScoped
public class DataInitializer {

    @Inject UserService userService;
    @Inject SportsFacilityService sportsFacilityService;
    @Inject RentalService rentalService;
    @Inject MongoClient mongoClient;

    void onStart(@Observes StartupEvent ev) {
        try {
            initData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initData() throws Exception {
        MongoDatabase db = mongoClient.getDatabase("sportsfacility");

        try { db.getCollection("users").drop(); } catch (Exception e) {}
        try { db.getCollection("facilities").drop(); } catch (Exception e) {}
        try { db.getCollection("rentals").drop(); } catch (Exception e) {}

        Document userJsonSchema = new Document("bsonType", "object")
                .append("required", Arrays.asList("login", "firstName", "lastName"))
                .append("properties", new Document()
                        .append("login", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 4)
                                .append("maxLength", 20))
                        .append("firstName", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 3)
                                .append("maxLength", 20))
                        .append("lastName", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 4)
                                .append("maxLength", 20))
                );

        ValidationOptions userValidation = new ValidationOptions().validator(new Document("$jsonSchema", userJsonSchema));
        db.createCollection("users", new CreateCollectionOptions().validationOptions(userValidation));

        db.getCollection("users").createIndex(Indexes.ascending("login"), new IndexOptions().unique(true));


        Document facilityJsonSchema = new Document("bsonType", "object")
                .append("required", Arrays.asList("name", "pricePerHour", "capacity"))
                .append("properties", new Document()
                        .append("name", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 3)
                                .append("maxLength", 20))
                        .append("pricePerHour", new Document()
                                .append("bsonType", "double")
                                .append("minimum", 0.0))
                        .append("capacity", new Document()
                                .append("bsonType", "int")
                                .append("minimum", 1))
                );
        ValidationOptions facilityValidation = new ValidationOptions().validator(new Document("$jsonSchema", facilityJsonSchema));
        db.createCollection("facilities", new CreateCollectionOptions().validationOptions(facilityValidation));


        Document rentalJsonSchema = new Document("bsonType", "object")
                .append("required", Arrays.asList("clientId", "facilityId", "startTime", "endTime"))
                .append("properties", new Document()
                        .append("clientId", new Document().append("bsonType", "string").append("minLength", 1))
                        .append("facilityId", new Document().append("bsonType", "string").append("minLength", 1))
                        .append("startTime", new Document().append("bsonType", "date"))
                        .append("endTime", new Document().append("bsonType", "date"))
                );
        ValidationOptions rentalValidation = new ValidationOptions().validator(new Document("$jsonSchema", rentalJsonSchema));
        db.createCollection("rentals", new CreateCollectionOptions().validationOptions(rentalValidation));


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

        userService.activateUser(client1.getHexId());
        userService.activateUser(client2.getHexId());

        try {
            rentalService.rentFacility(
                    client1.getHexId(),
                    court1.getHexId(),
                    LocalDateTime.now().plusDays(2).withHour(10).withMinute(0),
                    LocalDateTime.now().plusDays(2).withHour(11).withMinute(0)
            );

            rentalService.rentFacility(
                    client2.getHexId(),
                    pool1.getHexId(),
                    LocalDateTime.now().plusDays(3).withHour(18).withMinute(0),
                    LocalDateTime.now().plusDays(3).withHour(19).withMinute(0)
            );

        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia przykładowych rezerwacji: " + e.getMessage());
        }
    }
}