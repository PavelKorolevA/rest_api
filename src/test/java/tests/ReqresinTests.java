package tests;

import models.LombokUserData;
import models.JsonUserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static spec.Specs.request;
import static spec.Specs.responseSpec200;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresinTests extends TestBase{
//
    @Test
    @DisplayName("Successful registration")
    void successfulRegister() {

        JsonUserData jsonUserData = new JsonUserData();
        jsonUserData.setEmail("eve.holt@reqres.in");
        jsonUserData.setPassword("pistol");

        JsonUserData response = given()
                .spec(request)
                .body(jsonUserData)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(JsonUserData.class);

        assertEquals("4", response.getId());
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }

    @Test
    @DisplayName("Successful authorization")
    void successfulLogin() {
        JsonUserData jsonUserData = new JsonUserData();
        jsonUserData.setEmail("eve.holt@reqres.in");
        jsonUserData.setPassword("cityslicka");

        JsonUserData response = given()
                .spec(request)
                .body(jsonUserData)
                .when()
                .post("/login")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(JsonUserData.class);

        assertEquals(response.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Test
    @DisplayName("Unsuccessful authorization")
    void unsuccessfulLogin() {
        JsonUserData jsonUserData = new JsonUserData();
        jsonUserData.setEmail("peter@klaven");

        JsonUserData response = given()
                .spec(request)
                .body(jsonUserData)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .log().body()
                .extract().as(JsonUserData.class);

        assertEquals(response.getError(), "Missing password");
    }

    @Test
    @DisplayName("Search for a user")
    void singleUser() {

        LombokUserData response = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(LombokUserData.class);

        assertEquals("2", response.getJsonUserData().getId());
        assertEquals("janet.weaver@reqres.in", response.getJsonUserData().getEmail());
    }
}
