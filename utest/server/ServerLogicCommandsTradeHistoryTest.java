package server;

import common.transport.JSONAction;
import org.junit.jupiter.api.Test;
import server.dal.OrgUnitDbMock;
import server.dal.TradeHistoryDbMock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerLogicCommandsTradeHistoryTest {
    private TradeHistoryDbMock db;

    /**
     * Tests for getting Trade History
     */
    @Test
    public void testGetTradeHistoryWhenNotLoggedIn() throws Exception {
        db = new TradeHistoryDbMock(null, 0, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getGraphHistory(false, obj, db));
    }

    @Test
    public void testGetTradeHistoryWhenLoggedInAnNull() throws Exception {
        db = new TradeHistoryDbMock(null, 0, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Error occurred in retrieving trade history, check its name!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getGraphHistory(true, obj, db));
    }
}


