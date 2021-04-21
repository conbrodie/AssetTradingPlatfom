package AssetTradingPlatform;

import static  org.junit.jupiter.api.Assertions.*;
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
    /**
     * Test for createUser Method
     */
//    @Test
//    public void testSuccessCreateUser() throws CredentialException {
//        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
//        assertEquals("Logged in", user.Login("Kate","123"), "Failed to create user") ;
//    }
//    @Test
//    public void testAddingSpecialCharacters() throws CredentialException {
//        user.createUser("Kate@90", "123", "1@yahoo.com","ComputerClusterDivision");
//        assertEquals("Logged in", user.Login("Kate@90","123"),"Creating user failed");
//    }
//    @Test
//    public void testGivingMaxStringLengthOfUserName() throws CredentialException {
//        user.createUser("kkkkkkkkkkkkkkkk", "1", "kate123@gmail.com","ComputerClusterDivision");
//        assertEquals("Logged in", user.Login("kkkkkkkkkkkkkkkk","1"),"Creating user failed");
//
//    }
//    @Test
//    public void testTheSameOfUseNameAndPassword() throws CredentialException {
//        user.createUser("Kate123", "Kate123", "kate@gmail.com","ComputerClusterDivision");
//        assertEquals("Logged in", user.Login("Kate123","Kate123"),"Creating user failed");
//    }
//    @Test
//    public void testLeaveUserNameBlank(){
//        assertThrows(CredentialException.class, () ->{
//            user.createUser("", "123", "kate@gmail.com","ComputerClusterDivision");
//        });
//    }
//    @Test
//    public void testLeavePasswordBlank(){
//        assertThrows(CredentialException.class, () ->{
//            user.createUser("Kate", "", "kate@gmail.com","ComputerClusterDivision");
//        });
//    }
//    @Test
//    public void testLeaveEmailBlank(){
//        assertThrows(CredentialException.class, () ->{
//            user.createUser("Kate", "123", "","ComputerClusterDivision");
//        });
//    }
//    @Test
//    public void testLeaveOrganisationBlank(){
//        assertThrows(CredentialException.class, () ->{
//            user.createUser("Kate", "123", "kate@gmail.com","");
//        });
//    }
//    @Test
//    public void testTooLongStringLength(){
//        assertThrows(CredentialException.class, () -> {
//            user.createUser("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "123", "kate@gmail.com","ComputerClusterDivision");
//        });
//    }
//    @Test
//    public void testUsernameAlreadyExist(){
//        assertThrows(CredentialException.class, () -> {
//            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
//            user.createUser("Kate", "1", "kate1@gmail.com","HR Management");
//        });
//    }
//    @Test
//    public void testInvalidUserName(){
//        assertThrows(CredentialException.class, () -> {
//            user.createUser("kate:&/", "123", "kate.12","ComputerClusterDivision");
//        });
//    }
//    @Test
//    public void testInvalidEmail(){
//        assertThrows(CredentialException.class, () -> {
//            user.createUser("Kate", "123", "kate.12","ComputerClusterDivision");
//        });
//    }
    /**
     * Test for Login Method
     */
    @Test
    public void testSuccessLogin() throws CredentialException {
        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
        assertEquals("Logged in", user.Login("Kate","123"), "Failed to login");
    }
    @Test
    public void testLeaveNameBlank(){
        assertThrows(CredentialException.class, () ->{
            user.Login("","123");
        });
    }
    @Test
    public void testLeavePwBlank(){
        assertThrows(CredentialException.class, () ->{
            user.Login("Kate","");
        });
    }
    @Test
    public void testIncorrectUserName(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.Login("Kate1","123");
        });
    }
    @Test
    public void testIncorrectPassword(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.Login("Kate","1234");
        });
    }
    /**
     * Test for Logout Method
     */
    @Test
    public void testSuccessLogout() throws CredentialException {
        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
        user.Login("Kate","123");
        assertEquals("Logged out", user.Logout("Kate"), "Failed to logout");
    }
}

