package AssetTradingPlatform.server.test;

import static  org.junit.jupiter.api.Assertions.*;

import AssetTradingPlatform.common.Order;
import AssetTradingPlatform.common.User;
import AssetTradingPlatform.server.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.CredentialException;

public class TestUser {
    private AssetTradingPlatform.server.DB DB;
    private UserController user;
    @BeforeEach
    public void setup() throws CredentialException {
        DB = new MockDB();
        user = new UserController(DB);
    }
    /**
     * Test for createUser Method
     */
    @Test
    public void testSuccessCreateUser() throws CredentialException {
        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
        assertEquals("Logged in", user.login("Kate","123"), "Failed to create user") ;
    }
    @Test
    public void testAddingSpecialCharacters() throws CredentialException {
        user.createUser("Kate@90", "123", "1@yahoo.com","ComputerClusterDivision");
        assertEquals("Logged in", user.login("Kate@90","123"),"Creating user failed");
    }
    @Test
    public void testGivingMaxStringLengthOfUserName() throws CredentialException {
        user.createUser("kkkkkkkkkkkkkkkk", "1", "kate123@gmail.com","ComputerClusterDivision");
        assertEquals("Logged in", user.login("kkkkkkkkkkkkkkkk","1"),"Creating user failed");

    }
    @Test
    public void testTheSameOfUseNameAndPassword() throws CredentialException {
        user.createUser("Kate123", "Kate123", "kate@gmail.com","ComputerClusterDivision");
        assertEquals("Logged in", user.login("Kate123","Kate123"),"Creating user failed");
    }
    @Test
    public void testLeaveUserNameBlank(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("", "123", "kate@gmail.com","ComputerClusterDivision");
        });
    }
    @Test
    public void testLeavePasswordBlank(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "", "kate@gmail.com","ComputerClusterDivision");
        });
    }
    @Test
    public void testLeaveEmailBlank(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "","ComputerClusterDivision");
        });
    }
    @Test
    public void testLeaveOrganisationBlank(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","");
        });
    }
    @Test
    public void testTooLongStringLength(){
        assertThrows(CredentialException.class, () -> {
            user.createUser("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "123", "kate@gmail.com","ComputerClusterDivision");
        });
    }
    @Test
    public void testUsernameAlreadyExist(){
        assertThrows(CredentialException.class, () -> {
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.createUser("Kate", "1", "kate1@gmail.com","HR Management");
        });
    }
    @Test
    public void testInvalidUserName(){
        assertThrows(CredentialException.class, () -> {
            user.createUser("kate:&/", "123", "kate.12","ComputerClusterDivision");
        });
    }
    @Test
    public void testInvalidEmail(){
        assertThrows(CredentialException.class, () -> {
            user.createUser("Kate", "123", "kate.12","ComputerClusterDivision");
        });
    }
    /**
     * Test for login Method
     */
    @Test
    public void testSuccesslogin() throws CredentialException {
        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
        assertEquals("Logged in", user.login("Kate","123"), "Failed to login");
    }
    @Test
    public void testLeaveNameBlank(){
        assertThrows(CredentialException.class, () ->{
            user.login("","123");
        });
    }
    @Test
    public void testLeavePwBlank(){
        assertThrows(CredentialException.class, () ->{
            user.login("Kate","");
        });
    }
    @Test
    public void testIncorrectUserName(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.login("Kate1","123");
        });
    }
    @Test
    public void testIncorrectPassword(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.login("Kate","1234");
        });
    }
    /**
     * Test for logout Method
     */
    @Test
    public void testSuccessLogout() throws CredentialException {
        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
        user.login("Kate","123");
        assertEquals(true, user.logout("Kate"), "Failed to logout");
    }
    /**
     * Test for ResetPassword Method
     */
    @Test
    public void testSuccessChangePassword() throws CredentialException {
        user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
        user.login("Kate","123");
        assertEquals(true, user.changePassword("Kate", "123","1"), "Failed to change password");
    }
    @Test
    public void testLeaveNewPasswordBlank(){
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.login("Kate","123");
            user.changePassword("Kate", "123","");
        });
    }
    @Test
    public void testLeaveOldPasswordBlank() {
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.login("Kate","123");
            user.changePassword("Kate","","1");
        });
    }
    @Test
    public void testUserEnterAnOldPassword() {
        assertThrows(CredentialException.class, () ->{
            user.createUser("Kate", "123", "kate@gmail.com","ComputerClusterDivision");
            user.login("Kate","123");
            user.changePassword("Kate", "123","123");
        });
    }


}

