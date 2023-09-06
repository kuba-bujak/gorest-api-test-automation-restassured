package pl.globallogic.verification;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class ConfigurationVerificationTest {

    protected Logger logger = LoggerFactory.getLogger(ConfigurationVerificationTest.class);

    @Test
    public void shouldLogRequestAndResponseData() {
        logger.info("Verify test automation framework configuration test");
        RestAssured.useRelaxedHTTPSValidation();
        int expectedStatusCode = 200;
        Response response = get("https://gorest.co.in/public/v2/users");
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }
}
