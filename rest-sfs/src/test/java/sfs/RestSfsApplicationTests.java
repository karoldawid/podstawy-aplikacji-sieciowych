package sfs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RestSfsApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCRUUser(){
        String userJson = """
                {
                    "login": "milka2016",
                    "firstName": "Milka",
                    "lastName": "Maltanka",
                    "userType": "CLIENT"
                }
                """;

        // CREATE
        String newUserIdString = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/users/create")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        UUID newUserId = UUID.fromString(newUserIdString);

        // READ
        given()
                .when()
                .get("/api/v1/users/{id}", newUserId)
                .then()
                .statusCode(200)
                .body("login", equalTo("milka2016"))
                .body("firstName", equalTo("Milka"))
                .body("lastName", equalTo("Maltanka"));

        //UPDATE
        String updateUserJson = """
        {
            "firstName": "Miśka",
            "lastName": "Jasperska"
        }
    """;
        given()
                .contentType(ContentType.JSON)
                .body(updateUserJson)
                .when()
                .put("/api/v1/users/{id}", newUserId)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/v1/users/{id}", newUserId)
                .then()
                .statusCode(200)
                .body("login", equalTo("milka2016"))
                .body("firstName", equalTo("Miśka"))
                .body("lastName", equalTo("Jasperska"));
    }

    @Test
    void shouldCRUDFacility() {
        // CREATE
        String facilityJson = """
            {
                "name": "Siłownia Błysk",
                "pricePerHour": 75.0,
                "capacity": 50,
                "facilityType": "GYM",
                "areaInSqm": 300,
                "hasSauna": true
            }
        """;

        String newFacilityIdString = given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when()
                .post("/api/v1/facilities/create")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        UUID newFacilityId = UUID.fromString(newFacilityIdString);

        // READ
        given()
                .when()
                .get("/api/v1/facilities/{id}", newFacilityId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Siłownia Błysk"))
                .body("areaInSqm", equalTo(300));

        // UPDATE
        String updateFacilityJson = """
            {
                "name": "Siłownia Po Remoncie",
                "pricePerHour": 80.0,
                "capacity": 60
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(updateFacilityJson)
                .when()
                .put("/api/v1/facilities/{id}", newFacilityId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Siłownia Po Remoncie"))
                .body("capacity", equalTo(60));

        // DELETE
        given()
                .when()
                .delete("/api/v1/facilities/{id}", newFacilityId)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/v1/facilities/{id}", newFacilityId)
                .then()
                .statusCode(400);
    }


    @Test
    void shouldCreateAllocation() {
        String clientJson = """
            { "login": "testclient", "firstName": "Test", "lastName": "Client", "userType": "CLIENT" }
        """;
        UUID clientId = UUID.fromString(given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when().post("/api/v1/users/create")
                .then().statusCode(200).extract().path("id"));

        given()
                .when().put("/api/v1/users/activate/{id}", clientId)
                .then().statusCode(200);

        String facilityJson = """
            { "name": "Kort do Rezerwacji", "pricePerHour": 100, "capacity": 4, "facilityType": "TENNIS_COURT", "surfaceType": "CLAY", "isIndoor": true }
        """;
        UUID facilityId = UUID.fromString(given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when().post("/api/v1/facilities/create")
                .then().statusCode(200).extract().path("id"));

        String rentalJson = String.format("""
        {
            "clientId": "%s",
            "facilityId": "%s",
            "startTime": "%s",
            "endTime": "%s"
        }
    """, clientId, facilityId, "2099-12-01T10:00:00", "2099-12-01T11:00:00");

        given()
                .contentType(ContentType.JSON)
                .body(rentalJson)
                .when()
                .post("/api/v1/rentals/rent")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("clientId", equalTo(clientId.toString()));
    }

    @Test
    void shouldReturn400OnUserSyntaxViolation() {
        String badUserJson = """
            {
                "login": "", 
                "firstName": "Jan",
                "lastName": "Kowalski",
                "userType": "CLIENT"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(badUserJson)
                .when()
                .post("/api/v1/users/create")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturn400OnFacilitySyntaxViolation() {
        String badFacilityJson = """
            {
                "name": "Zła Siłownia",
                "pricePerHour": -100.0, 
                "capacity": 50,
                "facilityType": "GYM"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(badFacilityJson)
                .when()
                .post("/api/v1/facilities/create")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturn400OnUserUniquenessViolation() {
        String userJson = """
            { "login": "unikalnyLogin", "firstName": "Test", "lastName": "User", "userType": "CLIENT" }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/users/create")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/users/create")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturn400OnAllocationConflict() {
        String clientJson = """
            { "login": "renter", "firstName": "Test", "lastName": "Renter", "userType": "CLIENT" }
        """;
        UUID clientId = UUID.fromString(given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when().post("/api/v1/users/create")
                .then().statusCode(200).extract().path("id"));

        given()
                .when().put("/api/v1/users/activate/{id}", clientId)
                .then().statusCode(200);

        String facilityJson = """
            { "name": "Kort Do Konfliktu", "pricePerHour": 100, "capacity": 4, "facilityType": "TENNIS_COURT", "surfaceType": "HARD", "isIndoor": false }
        """;
        UUID facilityId = UUID.fromString(given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when().post("/api/v1/facilities/create")
                .then().statusCode(200).extract().path("id"));

        String rentalJson = String.format("""
        {
            "clientId": "%s",
            "facilityId": "%s",
            "startTime": "%s",
            "endTime": "%s"
        }
    """, clientId, facilityId, "2099-12-01T10:00:00", "2099-12-01T11:00:00");

        given()
                .contentType(ContentType.JSON)
                .body(rentalJson)
                .when()
                .post("/api/v1/rentals/rent")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body(rentalJson)
                .when()
                .post("/api/v1/rentals/rent")
                .then()
                .statusCode(400);
    }
}
