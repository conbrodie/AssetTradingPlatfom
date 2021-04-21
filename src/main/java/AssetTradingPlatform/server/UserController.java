package AssetTradingPlatform.server;

import javax.security.auth.login.CredentialException;

public class UserController {
    private final AssetTradingPlatform.server.DB DB;
    /**
     * Create a user management object
     * @param DB the user database
     */
    public UserController(DB DB){
        this.DB = DB;
    }

    /**
     * Register a new User
     * A new user consisting of a username, email and password password is created and saved to database
     * @param userName New user userName.
     * @param password New user password
     * @param email New user email
     * @param organisation New user's organisation
     * @throws javax.security.auth.login.CredentialException Username already exists
     * @throws javax.security.auth.login.CredentialException Email already exists
     * @return
     */
    public String createUser(String userName, String password, String email, String organisation) throws javax.security.auth.login.CredentialException {
        return "";
    }


    /**
     * Login a User
     * When logging in a User, an email and password are required.
     * Users entered password should be hashed and compared to the saved hashed password associated to the same username
     * @param userName A users userName.
     * @param password A users password
     * @throws javax.security.auth.login.CredentialException Incorrect credentials
     * @throws javax.security.auth.login.CredentialException User Doesn't Exist
     */
    public String login(String userName, String password) throws javax.security.auth.login.CredentialException {
        return "";
    }


    /**
     * Logout a User
     * When logging out a User, the active users email is required.
     * @param userName A users userName
     */
    public boolean logout(String userName){
        return false;
    }


    /**
     * Reset a Users password
     * Should retrieve email associated with the attempted login username and send link to reset password
     * Should not reveal associated email to which reset was sent only display message "Email sent"
     * @param userName A users userName
     * @throws javax.security.auth.login.CredentialException User Doesn't Exist
     */
    public void resetPassword(String userName) throws CredentialException {

    }

    /**
     * Change a Users password if their login matches.
     * @param userName the username.
     * @param oldPassword the old password.
     * @param newPassword the new password.
     */
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        return false;
    }


    /**
     * Removes existing User
     * Should only be possible by admin
     * @param userName A users userName
     */
    public void removeUser(String userName){

    }


    /**
     * Adds Admin status to user
     * @param userName A users userName
     */
    public void addAdmin(String userName){
    }


    /**
     * Removes admin status from user
     * @param userName A users userName
     */
    public void removeAdmin(String userName){

    }


    /**
     * Checks if user is Admin
     * @param userName A users userName
     * @return isAdmin Is user an admin
     */
    public boolean adminUser(String userName){
        return false;
    }

}
