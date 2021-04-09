package AssetTradingPlatform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.CredentialException;

public class TestUser {
    private UserDB userDB;
    private User user;
    @BeforeEach
    public void setup() throws CredentialException {
        userDB = new MockUserDB();
        user = new User(userDB);
    }
    @Test
    public void testSuccessCreateUser() throws CredentialException {
        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
        user.Login("Kate","123");
    }

}

