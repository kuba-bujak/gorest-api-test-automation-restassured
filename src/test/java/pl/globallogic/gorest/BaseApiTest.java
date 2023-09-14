package pl.globallogic.gorest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    protected static Logger logger = LoggerFactory.getLogger(BaseApiTest.class);
    private static final String BASE_URL = "https://gorest.co.in";
    private static final String BASE_PATH = "/public/v2";
    private static String token = "56ad20c9a5e919880722cc3700318ae818d253da99b7aff59b328e9b303ce777";


    @BeforeClass(alwaysRun = true)
    public void globalSetUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = BASE_PATH;
        RestAssured.requestSpecification = new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                addHeader("Authorization", "Bearer " + token).
                build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .build();
    }
}
