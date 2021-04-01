package AssetTradingPlatform;

/**
 * A User
 * @param userName A users userName.
 * @param password A users password
 * @param email A users email
 */
public class User {
    public String userName;
    public String password;
    public String email;


    /**
     * Create/Register a User
     * A new user consisting of a username, email and password password is created and saved to database
     * @param userName New user userName.
     * @param password New user password
     * @param email New user email
     * @throws UserNameException if username already exists
     * @throws EmailException if email already exists
     */
    public User(String userName, String password, String email){

    }


    /**
     * Login a User
     * When loging in a User, an email and password are required.
     * Users entered password should be hashed and compared to the saved hashed password associated to the same username
     * @param userName A users userName.
     * @param password A users password
     * @throws LoginException Incorrect credentials
     * @throws UserNameException User Doesn't Exist
     */
    public void Login(String userName, String password){

    }


    /**
     * Logout a User
     * When logging out a User, the active users email is required.
     * @param userName A users userName
     */
    public void Logout(String userName){

    }


    /**
     * Reset a Users password
     * Should retrieve email associated with the attempted login username and send link to reset password
     * Should not reveal associated email to which reset was sent only display message "Email sent"
     * @param userName A users userName
     * @throws UserNameException User Doesn't Exist
     */
    public void ResetPassword(String userName){

    }

}