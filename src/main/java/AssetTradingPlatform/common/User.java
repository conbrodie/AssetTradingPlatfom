package AssetTradingPlatform.common;

import AssetTradingPlatform.server.DB;

import javax.security.auth.login.CredentialException;
import java.util.Set;


/**
 * A class to manage user details and functions related to a user
 */
public class User {
    public int user_id;
    public String username;
    public String password;
    public Privilege user_type;
    public int org_unit_id;


    /**
     * Register a new User
     * A new user consisting of a username, email and password password is created and saved to database
     * @param username New user userName.
     * @param password New user password
     * @param user_type Type of User and associated privileges
     * @param org_unit_id Organisational Unit the User is a part of
     * @throws CredentialException Username already exists
     * @throws CredentialException Email already exists
     * @throws CredentialException User Type doesn't exist
     * @throws CredentialException Org doesn't exist
     */
    public User(String username, String password, Privilege user_type, int org_unit_id) throws CredentialException{

    }

}