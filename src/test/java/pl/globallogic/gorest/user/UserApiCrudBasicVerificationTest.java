package pl.globallogic.gorest.user;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.globallogic.gorest.dto.CreateUserRequestDTO;
import pl.globallogic.gorest.model.GoRestUser;
import pl.globallogic.gorest.user.testdata.UserApiTestDataGenerator;

import java.util.List;

public class UserApiCrudBasicVerificationTest extends BaseUserApiTest {

    private String ourUserId;

    @BeforeMethod(onlyForGroups = {"update", "delete"})
    public void testSetUp() {
        CreateUserRequestDTO userPayload = UserApiTestDataGenerator.getRandomUser();
        ourUserId = String.valueOf(userAPI.createUser(userPayload).getId());
        logger.info("Created user id: {}", ourUserId);
    }

    @AfterMethod(onlyForGroups = {"update"})
    public void testCleanUp() {
        logger.info("Deleting user with id '{}' after testing completion", ourUserId);
        userAPI.deleteUser(ourUserId);
    }

    @Test(groups = {"fetch"})
    public void shouldFetchAllUsersFromDefaultPageBodyExtract() {
        int expectedListLength = 10;
        List<GoRestUser> users = userAPI.getAllUsers();
        logger.info("Users : {}", users);
        Assert.assertEquals(expectedListLength, users.size());
    }

    @Test(groups={"update"})
    public void userDataShouldContainId() {
        GoRestUser ivan = userAPI.getUser(ourUserId);
        String expectedUserName = "Ivan Paulouski";
        Assert.assertEquals(expectedUserName, ivan.getName());
    }

    @Test(groups={"update"})
    public void shouldCreateAUserAndReturnAnId() {
        var userPayload = UserApiTestDataGenerator.getRandomUser();
        GoRestUser user = userAPI.createUser(userPayload);
        logger.info("User object : {}", user);
        Assert.assertNotEquals(user.getId(), 0);
    }

    @Test(groups={"update"})
    public void shouldUpdateExistingUserWithNewData() {
        CreateUserRequestDTO userPayload = UserApiTestDataGenerator.getRandomUser();
        GoRestUser updatedUser = userAPI.updateUserInfo(ourUserId, userPayload);
        Assert.assertEquals(userPayload.email(), updatedUser.getEmail());
    }

    @Test(groups={"delete"})
    public void shouldDeleteUserUsingId() {
        Assert.assertTrue(userAPI.deleteUser(ourUserId));
    }
}
