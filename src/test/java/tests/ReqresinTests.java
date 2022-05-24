package tests;

import models.LombokUserData;
import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static Spec.Specs.request;
import static Spec.Specs.responseSpec200;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresinTests extends TestBase{
//
    @Test
    @DisplayName("Successful registration")
    void successfulRegister() {

        models.User user = new User();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(User.class);

        assertEquals("4", response.getId());
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }

    @Test
    @DisplayName("Successful authorization")
    void successfulLogin() {
        User user = new User();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("cityslicka");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .post("/login")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(User.class);

        assertEquals(response.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Test
    @DisplayName("Unsuccessful authorization")
    void unsuccessfulLogin() {
        User user = new User();
        user.setEmail("peter@klaven");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .log().body()
                .extract().as(User.class);

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

        assertEquals("2", response.getUser().getId());
        assertEquals("janet.weaver@reqres.in", response.getUser().getEmail());
    }
}
