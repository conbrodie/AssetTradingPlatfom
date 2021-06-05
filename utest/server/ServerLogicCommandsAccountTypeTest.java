package server;

import static  org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import server.dal.AccountTypeDbMock;

public class ServerLogicCommandsAccountTypeTest {
    private AccountTypeDbMock db;
    private static String DB_FAIL = "{\"errorMessage\":\"Error when trying to retrieve account types.\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";

    @Test
    public void testGetAccountsWithNotLoggedIn() throws JsonProcessingException {
        db = new AccountTypeDbMock(null);
        assertEquals("{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}",
                ServerLogicCommands.getAccounts(false, db));

    }

    @Test
    public void testGetAccountsWithLoggedInAndNull() throws JsonProcessingException {
        db = new AccountTypeDbMock(null);
        assertEquals(DB_FAIL,
                ServerLogicCommands.getAccounts(true, db));

    }
    @Test
    public void testGetAccountsWithLoggedInAndEmpty() throws JsonProcessingException {
        db = new AccountTypeDbMock("");
        assertEquals(DB_FAIL,
                ServerLogicCommands.getAccounts(true, db));

    }

    @Test
    public void testGetAccountsWithLoggedInAndValidResult() throws JsonProcessingException {
        db = new AccountTypeDbMock("[{\"account_type_id\":1,\"account_type\":\"admin\"},{\"account_type_id\":2,\"account_type\":\"user\"}]");
        assertEquals("{\"errorMessage\":null,\"message\":\"Account types have been retrieved.\",\"resultSet\":\"[{\\\"account_type_id\\\":1,\\\"account_type\\\":\\\"admin\\\"},{\\\"account_type_id\\\":2,\\\"account_type\\\":\\\"user\\\"}]\",\"resultSetType\":\"json\",\"success\":\"1\"}",
                ServerLogicCommands.getAccounts(true, db));

    }

}
