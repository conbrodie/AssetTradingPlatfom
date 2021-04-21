package AssetTradingPlatform.common;

import AssetTradingPlatform.server.DB;

import javax.security.auth.login.CredentialException;
import java.util.Set;


/**
 * A class to manage user details and functions related to a user
 */
public class User {
    public String userName;
    public String password;
    public String email;
    public Set<Privilege> privileges;


    /**
     * Register a new User
     * A new user consisting of a username, email and password password is created and saved to database
     * @param userName New user userName.
     * @param password New user password
     * @param email New user email
     * @throws CredentialException Username already exists
     * @throws CredentialException Email already exists
     */
    public User(String userName, String password, String email,Set<Privilege> privileges ) throws CredentialException{

    }

}