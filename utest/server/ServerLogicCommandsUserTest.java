package server;

import common.models.UserModel;
import common.transport.JSONAction;
import org.junit.jupiter.api.Test;
import server.dal.UserDbMock;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class ServerLogicCommandsUserTest {
    private UserDbMock db;

    @Test
    public void testCreateUserWhileNotLoggedIn() throws Exception {
        db = new UserDbMock(Boolean.FALSE, null,0);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageUser(false, obj, db));
    }

    @Test
    public void testCreateUserWhileLoggedInAndInvalid() throws Exception {
        db = new UserDbMock(Boolean.FALSE, null,0);
        JSONAction obj = new JSONAction("Manage_User", "create", new UserModel(0, "", "", 0,0), "UserModel");
        String expected = "{\"errorMessage\":\"User '' was not created.\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageUser(true, obj, db));
    }

    @Test
    public void testCreateUserWhileLoggedInAndValidResult() throws Exception {
        db = new UserDbMock(Boolean.TRUE, null,0);
        JSONAction obj = new JSONAction("Manage_User", "create", new UserModel(302, "my.user", "verySecurePassword", 1,1), "UserModel");
        String expected = "{\"errorMessage\":null,\"message\":\"User 'my.user' was created.\",\"success\":\"1\"}";
        assertEquals(expected, ServerLogicCommands.manageUser(true, obj, db));
    }

    @Test
    public void testCreateUserWhileLoggedInAndAlreadyExists() throws Exception {
        db = new UserDbMock(Boolean.TRUE, "jones.r",1);
        JSONAction obj = new JSONAction("Manage_User", "create", new UserModel(33, "jones.r", "verySecurePassword", 1,1), "UserModel");
        String expected = "{\"errorMessage\":\"Username 'jones.r' is not available.\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageUser(true, obj, db));
    }

}