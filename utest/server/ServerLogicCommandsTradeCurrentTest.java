package server;

import common.models.TradeCurrentModel;
import common.transport.JSONAction;
import org.junit.jupiter.api.Test;
import server.dal.TradeCurrentDbMock;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServerLogicCommandsTradeCurrentTest {
    TradeCurrentDbMock db;
    /**
     * Tests for getting current trades
     */
    @Test
    public void testGetTradesWhenNotLoggedIn() throws Exception {
        db = new TradeCurrentDbMock(null, null, null, null);
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getCurrentTrades(false, db));
    }

    @Test
    public void testGetTradesWithLoggedInAndNull() throws Exception {
        db = new TradeCurrentDbMock(null, null, null, null);
        String expected = "{\"errorMessage\":\"Error when trying to retrieve current trades.\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getCurrentTrades(true, db));
    }

    @Test
    public void testGetTradesWithLoggedInAnEmpty() throws Exception {
        db = new TradeCurrentDbMock("", null, null, null);
        String expected = "{\"errorMessage\":\"Error when trying to retrieve current trades.\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getCurrentTrades(true, db));
    }

    @Test
    public void testGetTradesWithLoggedInAndAvailableTrades() throws Exception {
        db = new TradeCurrentDbMock("[]", null, null, null);
        String expected = "{\"errorMessage\":null,\"message\":\"Current trades have been retrieved.\",\"resultSet\":\"[]\",\"resultSetType\":\"json\",\"success\":\"1\"}";
        assertEquals(expected, ServerLogicCommands.getCurrentTrades(true, db));
    }

    /**
     * Tests for creating current trades
     */
    @Test
    public void testCreateTradesWhenNotLoggedIn() throws Exception {
        db = new TradeCurrentDbMock(null, null, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\",\"tradeId\":0}";
        assertEquals(expected, ServerLogicCommands.addTrade(false, obj, db));
    }

    @Test
    public void testCreateTradesWithLoggedInAndInvalid() throws Exception {
        String[] result = new String[3];
        result[0] = "0";
        result[1] = "1";
        result[2] = "1";
        db = new TradeCurrentDbMock(null, result, null, null);
        JSONAction obj = new JSONAction("Add_Trade", "create", new TradeCurrentModel(1,"BUY", 1, "Compute Cluster Division", 2, "bob", 1, "CPU Hours", 1, 10, new Timestamp(1622873054735l)), "TradeCurrentModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":\"1\",\"message\":\"Trade was NOT created.\",\"success\":\"0\",\"tradeId\":0}";
        assertEquals(expected, ServerLogicCommands.addTrade(true, obj, db));
    }

    @Test
    public void testCreateTradesWithLoggedInAndVaild() throws Exception {
        String[] result = new String[3];
        result[0] = "3";
        result[1] = "1";
        result[2] = "1";
        db = new TradeCurrentDbMock(null, result, null, null);
        JSONAction obj = new JSONAction("Add_Trade", "create", new TradeCurrentModel(1,"BUY", 1, "Compute Cluster Division", 2, "bob", 1, "CPU Hours", 1, 10, new Timestamp(1622873054735l)), "TradeCurrentModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":null,\"message\":\"Trade was created.\",\"success\":\"1\",\"tradeId\":1}";
        assertEquals(expected, ServerLogicCommands.addTrade(true, obj, db));
    }

    /**
     * Tests for deleting current trades
     */
    @Test
    public void testDeleteTradesWhenNotLoggedIn() throws Exception {
        db = new TradeCurrentDbMock(null, null, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.deleteTrade(false, obj, db));
    }

    @Test
    public void testDeleteTradesWithLoggedInAndInvalid() throws Exception {
        db = new TradeCurrentDbMock(null, null, false, null);
        JSONAction obj = new JSONAction("Delete_Trade", "delete", new TradeCurrentModel(1,"BUY", 1, "Compute Cluster Division", 2, "bob", 1, "CPU Hours", 1, 10, new Timestamp(1622873054735l)), "TradeCurrentModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":\"Trade was not deleted.\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.deleteTrade(true, obj, db));
    }

    @Test
    public void testDeleteTradesWithLoggedInAndVaild() throws Exception {
        db = new TradeCurrentDbMock(null, null, true, null);
        JSONAction obj = new JSONAction("Delete_Trade", "delete", new TradeCurrentModel(1,"BUY", 1, "Compute Cluster Division", 2, "bob", 1, "CPU Hours", 1, 10, new Timestamp(1622873054735l)), "TradeCurrentModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":null,\"message\":\"Trade was deleted.\",\"success\":\"1\"}";
        assertEquals(expected, ServerLogicCommands.deleteTrade(true, obj, db));
    }
}
