package pl.globallogic.gorest.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pl.globallogic.gorest.apis.UserAPI;

import java.lang.reflect.Method;

public class BaseUserApiTest {

    protected static Logger logger = LoggerFactory.getLogger(BaseUserApiTest.class);

    protected UserAPI userAPI = new UserAPI();

    @BeforeClass(alwaysRun = true)
    public void globalSetUp(){
        logger.info("Put any global set up code here!!!");
        logger.warn("Be careful with global objects in test (RestAssured.requestSpecification, etc...)");
    }

    @BeforeMethod
    public void testSetUp(Method method) {
        logger.info("Running '{}' test", method.getName());
    }

    @AfterMethod
    public void testTearDown(Method method) {
        logger.info("Finishing '{}' test", method.getName());
    }

}
