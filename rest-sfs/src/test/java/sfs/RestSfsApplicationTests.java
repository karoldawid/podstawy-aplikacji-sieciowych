package sfs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestSfsApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        mongoTemplate.dropCollection("users");
        mongoTemplate.dropCollection("facilities");
        mongoTemplate.dropCollection("rentals");

        Index uniqueLoginIndex = new Index().on("login", Sort.Direction.ASC).unique();
        mongoTemplate.indexOps("users").createIndex(uniqueLoginIndex);
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
                    "lastName": "Maltanka"
                }
                """;

        String newUserId = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/users/clients")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertNotNull(newUserId);

        given()
                .when()
                .get("/api/v1/users/{id}", newUserId)
                .then()
                .statusCode(200)
                .body("login", equalTo("milka2016"))
                .body("firstName", equalTo("Milka"))
                .body("lastName", equalTo("Maltanka"));

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
        String facilityJson = """
            {
                "name": "Siłownia Błysk",
                "pricePerHour": 75.0,
                "capacity": 50,
                "areaInSqm": 300,
                "hasSauna": true
            }
        """;

        String newFacilityId = given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when()
                .post("/api/v1/facilities/gyms")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        assertNotNull(newFacilityId);

        given()
                .when()
                .get("/api/v1/facilities/{id}", newFacilityId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Siłownia Błysk"))
                .body("areaInSqm", equalTo(300));

        String updateFacilityJson = """
        {
            "name": "Siłownia Po Remoncie",
            "pricePerHour": 80.0,
            "capacity": 60,
            "areaInSqm": 300,
            "hasSauna": true
        }
    """;

        given()
                .contentType(ContentType.JSON)
                .body(updateFacilityJson)
                .when()
                .put("/api/v1/facilities/gyms/{id}", newFacilityId)
                .then().log().all()
                .statusCode(200)
                .body("name", equalTo("Siłownia Po Remoncie"))
                .body("capacity", equalTo(60));

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
            { "login": "testclient", "firstName": "Test", "lastName": "Client" }
        """;
        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when().post("/api/v1/users/clients")
                .then().statusCode(200).extract().path("id");

        given()
                .when().put("/api/v1/users/activate/{id}", clientId)
                .then().statusCode(200);

        String facilityJson = """
            { "name": "Kort do Rezerwacji", "pricePerHour": 100, "capacity": 4, "surfaceType": "CLAY", "isIndoor": true }
        """;
        String facilityId = given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when().post("/api/v1/facilities/tennis-courts")
                .then().log().all().statusCode(200).extract().path("id");

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
                .body("clientId", equalTo(clientId));
    }

    @Test
    void shouldReturn400OnUserSyntaxViolation() {
        String badUserJson = """
            {
                "login": "", 
                "firstName": "Jan",
                "lastName": "Kowalski"
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(badUserJson)
                .when()
                .post("/api/v1/users/clients")
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
                "areaInSqm": 100,
                "hasSauna": true
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(badFacilityJson)
                .when()
                .post("/api/v1/facilities/gyms")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturn400OnUserUniquenessViolation() {
        String userJson = """
            { "login": "unikalnyLogin", "firstName": "Test", "lastName": "User" }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/users/clients")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/users/clients")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturn400OnAllocationConflict() {
        String clientJson = """
            { "login": "renter", "firstName": "Test", "lastName": "Renter" }
        """;
        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when().post("/api/v1/users/clients")
                .then().statusCode(200).extract().path("id");

        given()
                .when().put("/api/v1/users/activate/{id}", clientId)
                .then().statusCode(200);

        String facilityJson = """
            { "name": "Kort Do Konfliktu", "pricePerHour": 100, "capacity": 4, "surfaceType": "HARD", "isIndoor": false }
        """;
        String facilityId = given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when().post("/api/v1/facilities/tennis-courts")
                .then().log().all().statusCode(200).extract().path("id");

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