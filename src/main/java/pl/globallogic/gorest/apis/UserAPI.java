package pl.globallogic.gorest.apis;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.globallogic.gorest.constants.ConfigurationParams;
import pl.globallogic.gorest.dto.CreateUserRequestDTO;
import pl.globallogic.gorest.model.GoRestUser;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;

public class UserAPI {

    protected Logger logger = LoggerFactory.getLogger(UserAPI.class);

    private final String endpoint = "/users";
    private RequestSpecification request;

    public UserAPI() {
        setUpRequest();
    }

    private void setUpRequest() {
        String token = System.getProperty("token");
        this.request = given()
                .baseUri(ConfigurationParams.BASE_URI)
                .basePath(ConfigurationParams.BASE_PATH)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token );
    }
    public List<GoRestUser> getAllUsers() {
        setUpRequest();
        logger.info("Fetching users information");
        Response resp = request.get(endpoint).then().extract().response();
        return resp.jsonPath().getList("", GoRestUser.class);
    }

    public GoRestUser getUser(String userId) {
        logger.info("Fetching data for user id '{}'", userId);
        setUpRequest();
        return request.pathParam("userId", userId).when()
                .get(endpoint + "/{userId}").then().extract().as(GoRestUser.class);
    }

    public GoRestUser createUser(CreateUserRequestDTO userPayload) {
        setUpRequest();
        logger.info("Creating new user with payload: {}", userPayload);
        return request.body(userPayload).when().post(endpoint).then().extract().response().as(GoRestUser.class);
    }

    public GoRestUser updateUserInfo(String userId, CreateUserRequestDTO userPayload) {
        setUpRequest();
        logger.info("Updating information for user with id '{}' using next payload: {}", userId, userPayload);
        return request.pathParam("userId", userId).body(userPayload).when().
                put(endpoint + "/{userId}").then().extract().response().as(GoRestUser.class);
    }

    public boolean deleteUser(String userId) {
        logger.info("Deleting user with id '{}'", userId);
        setUpRequest();
        return request.pathParam("userId", userId)
                .when().delete(endpoint + "/{userId}").getStatusCode() == 204;
    }
}
