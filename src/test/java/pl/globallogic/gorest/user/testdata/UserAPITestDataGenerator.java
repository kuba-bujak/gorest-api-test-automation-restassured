package pl.globallogic.gorest.user.testdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.globallogic.gorest.dto.CreateUserRequestDTO;

public class UserApiTestDataGenerator {

    protected static Logger logger = LoggerFactory.getLogger(UserApiTestDataGenerator.class);
    public static CreateUserRequestDTO getRandomUser() {
       return new CreateUserRequestDTO(getRandomEmail(), "Ivan Paulouski", "male", "active" );
    }

    private static String getRandomEmail() {
        String email = "ivan.email" + (int) (Math.random() * 67237464) + "@hotmail.com";
        logger.info("Generated email: {}", email);
        return email;
    }
}
