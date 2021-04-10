package AssetTradingPlatform;

import javax.security.auth.login.CredentialException;


/**
 * A class to manage user details and functions related to a user
 */
public class User {
    public String userName;
    public String password;
    public String email;
    public Boolean admin;
    private final UserDB userDB;

    /**
     * Create a user management object
     * @param userDB the user database
     */
    public User(UserDB userDB){
        this.userDB = userDB;
    }

    /**
     * Register a new User
     * A new user consisting of a username, email and password password is created and saved to database
     * @param userName New user userName.
     * @param password New user password
     * @param email New user email
     * @param organisation New user's organisation
     * @throws CredentialException Username already exists
     * @throws CredentialException Email already exists
     */
    public void createUser(String userName, String password,  String email, String organisation) throws CredentialException{

    }


    /**
     * Login a User
     * When logging in a User, an email and password are required.
     * Users entered password should be hashed and compared to the saved hashed password associated to the same username
     * @param userName A users userName.
     * @param password A users password
     * @throws CredentialException Incorrect credentials
     * @throws CredentialException User Doesn't Exist
     */
    public void Login(String userName, String password) throws CredentialException{

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
     * @throws CredentialException User Doesn't Exist
     */
    public void ResetPassword(String userName) throws CredentialException{

    }


    /**
     * Removes existing User
     * Should only be possible by admin
     * @param userName A users userName
     */
    public void RemoveUser(String userName){

    }


    /**
     * Adds Admin status to user
     * @param userName A users userName
     */
    public void AddAdmin(String userName){
    }


    /**
     * Removes admin status from user
     * @param userName A users userName
     */
    public void RemoveAdmin(String userName){

    }


    /**
     * Checks if user is Admin
     * @param userName A users userName
     * @return isAdmin Is user an admin
     */
    public boolean AdminUser(String userName){
        return false;
    }

}