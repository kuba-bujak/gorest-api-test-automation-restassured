package pl.globallogic.gorest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.globallogic.gorest.dto.CreateUserRequestDTO;
import pl.globallogic.gorest.dto.CreateUserResponseDTO;
import pl.globallogic.gorest.model.OurUser;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UserApiCrudBasicVerificationTest extends BaseApiTest{

    private static final String ENDPOINT = "/users";
    private String ourUserId;

    @BeforeMethod
    public void testSetUp() {
        String randomEmail = getRandomEmail();
        CreateUserRequestDTO userPayload =
                new CreateUserRequestDTO("Super User", randomEmail, "male", "active");
        Response res = given().
                body(userPayload).
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
        Response res =
            when().
                    get(ENDPOINT).
            then().
                    extract().response();

        List<OurUser> users = res.jsonPath().getList("", OurUser.class);
        logger.info("Users: {}", users);
        Assert.assertEquals(users.size(), expectedListLength);
    }

    @Test
    public void shouldFetchAllUsersFromDefaultPageAssertThat() {
       when().
                get(ENDPOINT).
       then().
               assertThat().
               body("name[0]", equalTo("Super User"));
    }

    // should list user data using user id
    @Test
    public void userDataShouldContainId() {
        String userId = ourUserId;
        given().
                pathParam("userId", userId).
        when().
                get( ENDPOINT + "/{userId}").
        then().
                assertThat().
                body("id", equalTo(Integer.valueOf(ourUserId)));
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
        Response res = given().
                body(userPayload).
        when().
                post(ENDPOINT).
        then().
                extract().response();
        CreateUserResponseDTO user = res.as(CreateUserResponseDTO.class);
        logger.info("User object : {}", user);
        Assert.assertNotNull(user.id());

    }
    // should update info with new information
    @Test
    public void shouldUpdateExistingUserWithNewData() {
        String randomEmail = getRandomEmail();
        String newName = "Super User";
        logger.info("New user name: {}", newName);
        CreateUserRequestDTO userPayload =
                new CreateUserRequestDTO(newName, randomEmail, "male", "active");
        given().
                pathParam("userId", ourUserId).
                body(userPayload).
        when().
                put(ENDPOINT + "/{userId}").
        then().
                assertThat().
                body("name", equalTo(newName));
    }
    // should delete user from system
    @Test
    public void shouldDeleteExistingUserUsingId() {
        Response res = given().
                pathParam("userId", ourUserId).
        when().
                delete(ENDPOINT + "/{userId}");
        int expectedStatusCode = 204;
        Assert.assertEquals(res.statusCode(), expectedStatusCode);
    }
}
