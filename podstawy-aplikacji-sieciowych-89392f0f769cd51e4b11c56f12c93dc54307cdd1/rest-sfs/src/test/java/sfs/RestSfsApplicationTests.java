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
    void shouldCreateFacilityManager() {
        String managerJson = """
            {
                "login": "managerCreate",
                "firstName": "Jan",
                "lastName": "Tworzyciel"
            }
        """;

        String newId = given()
                .contentType(ContentType.JSON)
                .body(managerJson)
                .when()
                .post("/api/v1/facility-managers")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/v1/users/{id}", newId)
                .then()
                .statusCode(200)
                .body("login", equalTo("managerCreate"))
                .body("firstName", equalTo("Jan"))
                .body("lastName", equalTo("Tworzyciel"))
                .body("active", equalTo(false));
    }

    @Test
    void shouldGetFacilityManagerById() {
        String managerJson = """
            { "login": "managerRead", "firstName": "Adam", "lastName": "Czytalski" }
        """;
        String id = given()
                .contentType(ContentType.JSON)
                .body(managerJson)
                .when().post("/api/v1/facility-managers")
                .then().statusCode(201).extract().path("id");

        given()
                .when()
                .get("/api/v1/users/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("login", equalTo("managerRead"))
                .body("firstName", equalTo("Adam"));
    }

    @Test
    void shouldUpdateFacilityManager() {
        String createJson = """
            { "login": "managerUpdate", "firstName": "Piotr", "lastName": "Zmianowski" }
        """;
        String id = given()
                .contentType(ContentType.JSON)
                .body(createJson)
                .when().post("/api/v1/facility-managers")
                .then().statusCode(201).extract().path("id");

        String updateJson = """
            { "firstName": "Paweł", "lastName": "Nowy" }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(updateJson)
                .when()
                .put("/api/v1/users/{id}", id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/v1/users/{id}", id)
                .then()
                .statusCode(200)
                .body("login", equalTo("managerUpdate"))
                .body("firstName", equalTo("Paweł"))
                .body("lastName", equalTo("Nowy"));
    }

    @Test
    void shouldCreateGym() {
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
                .body("areaInSqm", equalTo(300))
                .body("hasSauna", equalTo(true));
    }

    @Test
    void shouldGetGymById() {
        String facilityJson = """
            {
                "name": "Siłownia Odczyt",
                "pricePerHour": 60.0,
                "capacity": 40,
                "areaInSqm": 200,
                "hasSauna": false
            }
        """;
        String id = given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when().post("/api/v1/facilities/gyms")
                .then().statusCode(200).extract().path("id");

        given()
                .when()
                .get("/api/v1/facilities/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Siłownia Odczyt"))
                .body("capacity", equalTo(40));
    }

    @Test
    void shouldUpdateGym() {
        String createJson = """
            {
                "name": "Siłownia Stara",
                "pricePerHour": 50.0,
                "capacity": 30,
                "areaInSqm": 150,
                "hasSauna": false
            }
        """;
        String id = given()
                .contentType(ContentType.JSON)
                .body(createJson)
                .when().post("/api/v1/facilities/gyms")
                .then().statusCode(200).extract().path("id");

        String updateJson = """
            {
                "name": "Siłownia Nowa",
                "pricePerHour": 55.0,
                "capacity": 35,
                "areaInSqm": 150,
                "hasSauna": true
            }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(updateJson)
                .when()
                .put("/api/v1/facilities/gyms/{id}", id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/v1/facilities/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Siłownia Nowa"))
                .body("pricePerHour", equalTo(55.0f))
                .body("hasSauna", equalTo(true));
    }

    @Test
    void shouldDeleteGym() {
        String createJson = """
            {
                "name": "Siłownia Usuwana",
                "pricePerHour": 40.0,
                "capacity": 20,
                "areaInSqm": 100,
                "hasSauna": false
            }
        """;
        String id = given()
                .contentType(ContentType.JSON)
                .body(createJson)
                .when().post("/api/v1/facilities/gyms")
                .then().statusCode(200).extract().path("id");

        given()
                .when()
                .delete("/api/v1/facilities/{id}", id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/api/v1/facilities/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldCreateAllocation() {
        String clientJson = """
            { "login": "testclient", "firstName": "Test", "lastName": "Client" }
        """;
        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when().post("/api/v1/clients")
                .then().statusCode(201).extract().path("id");

        given()
                .when().put("/api/v1/users/{id}/activate", clientId)
                .then().statusCode(200);

        String facilityJson = """
            { "name": "Kort do Rezerwacji", "pricePerHour": 100, "capacity": 4, "surfaceType": "CLAY", "isIndoor": true }
        """;
        String facilityId = given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when().post("/api/v1/facilities/tennis-courts")
                .then().statusCode(200).extract().path("id");

        String rentalJson = String.format("""
        {
            "clientId": "%s",
            "facilityId": "%s",
            "startTime": "%s",
            "endTime": "%s"
        }
        """, clientId, facilityId, "2099-12-01T10:00:00", "2099-12-01T11:00:00");

        String rentalId = given()
                .contentType(ContentType.JSON)
                .body(rentalJson)
                .when()
                .post("/api/v1/rentals/rent")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("clientId", equalTo(clientId))
                .extract().path("id");

        given()
                .when()
                .get("/api/v1/rentals/client/{clientId}", clientId)
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(rentalId))
                .body("[0].facilityId", equalTo(facilityId));
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
                .post("/api/v1/clients")
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
    void shouldReturn409OnUserUniquenessViolation() {
        String userJson = """
            { "login": "unikalnyLogin", "firstName": "Test", "lastName": "User" }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/clients")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/api/v1/clients")
                .then()
                .statusCode(409);

        given()
                .when()
                .get("/api/v1/users/search/exact?login=unikalnyLogin")
                .then()
                .statusCode(200)
                .body("login", equalTo("unikalnyLogin"));
    }

    @Test
    void shouldReturn409OnAllocationConflict() {
        String clientJson = """
            { "login": "renter", "firstName": "Test", "lastName": "Renter" }
        """;
        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when().post("/api/v1/clients")
                .then().statusCode(201).extract().path("id");

        given()
                .when().put("/api/v1/users/{id}/activate", clientId)
                .then().statusCode(200);

        String facilityJson = """
            { "name": "Kort Do Konfliktu", "pricePerHour": 100, "capacity": 4, "surfaceType": "HARD", "isIndoor": false }
        """;
        String facilityId = given()
                .contentType(ContentType.JSON)
                .body(facilityJson)
                .when().post("/api/v1/facilities/tennis-courts")
                .then().statusCode(200).extract().path("id");

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
                .statusCode(409);

        given()
                .when()
                .get("/api/v1/rentals/client/{clientId}", clientId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    void shouldReturn404WhenRentingNonExistentFacility() {
        String clientJson = """
            { "login": "test404", "firstName": "Test", "lastName": "Missing" }
        """;
        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when().post("/api/v1/clients")
                .then().statusCode(201).extract().path("id");

        given().when().put("/api/v1/users/{id}/activate", clientId).then().statusCode(200);

        String nonExistentFacilityId = "ffffffff-ffff-ffff-ffff-ffffffffffff";

        String rentalJson = String.format("""
        {
            "clientId": "%s",
            "facilityId": "%s",
            "startTime": "2099-01-01T10:00:00",
            "endTime": "2099-01-01T11:00:00"
        }
        """, clientId, nonExistentFacilityId);

        given()
                .contentType(ContentType.JSON)
                .body(rentalJson)
                .when()
                .post("/api/v1/rentals/rent")
                .then()
                .statusCode(404);

        given()
                .when()
                .get("/api/v1/rentals/client/{clientId}", clientId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }
}