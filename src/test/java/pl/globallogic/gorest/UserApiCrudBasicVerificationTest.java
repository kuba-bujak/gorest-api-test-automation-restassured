package pl.globallogic.gorest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.globallogic.gorest.model.CreateUserRequestDTO;
import pl.globallogic.gorest.model.CreateUserResponseDTO;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UserApiCrudBasicVerificationTest extends BaseApiTest{

    private static final String ENDPOINT = "/users";
    private static String token = "56ad20c9a5e919880722cc3700318ae818d253da99b7aff59b328e9b303ce777";
    private String ourUserId;

    // token
    // fixtures jak w python

    @BeforeMethod
    public void testSetUp() {
        String randomEmail = getRandomEmail();
        CreateUserRequestDTO userPayload =
                new CreateUserRequestDTO("Super User", randomEmail, "male", "active");
        Response res = given().
                body(userPayload).
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
        when().
                post(ENDPOINT).
        then().extract().response();
        ourUserId = res.jsonPath().getString("id");
        logger.info("Created user id: {}", ourUserId);
    }

    // should fetch all users
    @Test
    public void shouldFetchAllUsersFromDefaultPageBodyExtract() {
        int expectedListLength = 10;
        Response res = given().
                log().all().
        when().
                get(ENDPOINT).
        then().extract().response();
        List<CreateUserResponseDTO> users = res.jsonPath().getList("", CreateUserResponseDTO.class);
        logger.info("Users: {}", users);
        Assert.assertEquals(users.size(), expectedListLength);
    }

    @Test
    public void shouldFetchAllUsersFromDefaultPageAssertThat() {
       given().
                log().all().
       when().
                get(ENDPOINT).
        then().
               assertThat()
               .body("name[0]", equalTo("Gov. Ajeet Mukhopadhyay"));

    }

    // should list user data using user id
    @Test
    public void userDataShouldContainId() {
        String userId = ourUserId;
        given().
                pathParam("userId", userId).
                header("Authorization", "Bearer " + token).
        when().
                get( ENDPOINT + "/{userId}").
        then().
                log().all();
    }

    // should create new user and return id
    private static String getRandomEmail() {
        String email = "superuser." + (int)(Math.random() * 2000) + "@gmail.com";
        logger.info("Generated email: {}", email);
        return email;
    }
    @Test
    public void shouldCreateUserAndReturnId() {
        CreateUserRequestDTO userPayload =
                new CreateUserRequestDTO("Super User", getRandomEmail(), "male", "active");
        given().
                body(userPayload).
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
        when().
                post(ENDPOINT).
        then().
                extract().response();

    }
    // should update info with new information
    @Test
    public void shouldUpdateExistingUserWithNewData() {
        String randomEmail = getRandomEmail();
        CreateUserRequestDTO userPayload =
                new CreateUserRequestDTO("Super User", randomEmail, "male", "active");
        given().
                pathParam("userId", ourUserId).
                body(userPayload).
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
        when().
                put(ENDPOINT + "/{userId}").
        then().
                log().all();
    }
    // should delete user from system
    @Test
    public void shouldDeleteExistingUserUsingId() {
        Response res = given().
                pathParam("userId", ourUserId).
                header("Authorization", "Bearer " + token).
        when().
                delete(ENDPOINT + "/{userId}");
        int expectedStatusCode = 204;
        Assert.assertEquals(res.statusCode(), expectedStatusCode);
    }
}
