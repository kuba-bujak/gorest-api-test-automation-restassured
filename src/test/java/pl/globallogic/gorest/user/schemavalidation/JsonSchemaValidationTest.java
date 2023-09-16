package pl.globallogic.gorest.user.schemavalidation;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.globallogic.gorest.user.BaseUserApiTest;
import pl.globallogic.gorest.apis.UserAPI;
import pl.globallogic.gorest.constants.ConfigurationParams;
import pl.globallogic.gorest.dto.CreateUserRequestDTO;

import static io.restassured.RestAssured.given;
import static pl.globallogic.gorest.user.testdata.UserApiTestDataGenerator.getRandomUser;

public class JsonSchemaValidationTest extends BaseUserApiTest {

    private static final String ENDPOINT = ConfigurationParams.BASE_URI
            + ConfigurationParams.BASE_PATH + "/users";

    private static String token = System.getProperty("token");

    private String ourUserId;

    @BeforeMethod
    public void testSetUp() {
        CreateUserRequestDTO userPayload = getRandomUser();
        ourUserId = String.valueOf(new UserAPI().createUser(userPayload).getId());
    }

    @Test
    public void responseShouldBeValidAgainstSchema() {
        logger.info("Validating response data against schema in '{}'",
                ConfigurationParams.JSON_SCHEMA_FILE);
        String userId = ourUserId;
        given().
                pathParam("userId", userId).
                contentType(ContentType.JSON).
                header("Authorization", "Bearer " + token).
        when().
                get(ENDPOINT + "/{userId}").
        then()
                .log().all().
                body( JsonSchemaValidator.matchesJsonSchemaInClasspath(ConfigurationParams.JSON_SCHEMA_FILE));
    }
}
