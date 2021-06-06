package server;

import common.models.TradeCurrentModel;
import common.models.TradeHistoryModel;
import common.transport.JSONAction;
import org.junit.jupiter.api.Test;
import server.dal.OrgUnitDbMock;
import server.dal.TradeHistoryDbMock;

import java.sql.Timestamp;

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
    public void testGetTradeHistoryByAssetLoggedInAndInvalidCount() throws Exception {
        String tradeHistory = "[]";
        db = new TradeHistoryDbMock(tradeHistory, 0, tradeHistory, null);
        JSONAction obj = new JSONAction("Get_Graph_History", "", new TradeHistoryModel(1,1,"BUY","Compute Cluster Division", "bob", "CPU Hour", 1, 10, new Timestamp(1622873054435l), new Timestamp(1622873054735l)), "TradeHistoryModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":\"Error occurred in retrieving trade history, check its name!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getGraphHistory(true, obj, db));
    }

    @Test
    public void testGetTradeHistoryByAssetLoggedInAndValid() throws Exception {
        String tradeHistory = "[{\"trade_id_sell\":1,\"trade_id_buy\":\"1\"trade_type\":BUY,\"org_unit_name\":\"Compute Cluster Division\"username\":bob,\"asset_name\":\"CPU Hours,\"quantity\":\"1,\"price\":\"10,\"trade_date\":\"2016-02-20T03:26:32+05:30,\"date_processed\":\"2016-02-20T03:26:32+05:30\"}]";
        db = new TradeHistoryDbMock(tradeHistory, 1, tradeHistory, null);
        JSONAction obj = new JSONAction("Get_Graph_History", "", new TradeHistoryModel(1,1,"BUY","Compute Cluster Division", "bob", "CPU Hours", 1, 10, new Timestamp(1622873054435l), new Timestamp(1622873054735l)), "TradeHistoryModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":null,\"message\":\"Trade history for asset 'CPU Hours' has been retrieved.\",\"resultSet\":\"[{\\\"trade_id_sell\\\":1,\\\"trade_id_buy\\\":\\\"1\\\"trade_type\\\":BUY,\\\"org_unit_name\\\":\\\"Compute Cluster Division\\\"username\\\":bob,\\\"asset_name\\\":\\\"CPU Hours,\\\"quantity\\\":\\\"1,\\\"price\\\":\\\"10,\\\"trade_date\\\":\\\"2016-02-20T03:26:32+05:30,\\\"date_processed\\\":\\\"2016-02-20T03:26:32+05:30\\\"}]\",\"resultSetType\":\"json\",\"success\":\"1\"}";
        assertEquals(expected, ServerLogicCommands.getGraphHistory(true, obj, db));
    }
}

