package server;

import common.models.AssetHoldingModel;
import common.transport.JSONAction;
import org.junit.jupiter.api.Test;
import server.dal.AssetHoldingDbMock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerLogicCommandsAssetHoldingTest {
    private AssetHoldingDbMock db;

    /**
     * Test for get asset holdings
     */
    @Test
    public void testGetAssetHoldingWhenNotLoggedIn() throws Exception {
        db = new AssetHoldingDbMock(null, null, null);
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getAssetHoldings(false, db));
    }

    @Test
    public void testGetAssetHoldingWithLoggedInAnNull() throws Exception {
        db = new AssetHoldingDbMock(null, null, null);
        String expected = "{\"errorMessage\":\"Error when trying to retrieve asset holdings.\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getAssetHoldings(true, db));
    }

    @Test
    public void testGetAssetHoldingWithLoggedInAndValidResult() throws Exception {
        db = new AssetHoldingDbMock("[{\"org_unit_id\":1,\"asset_id\":1,\"quantity\":1\"}]", null, null);
        assertEquals("{\"errorMessage\":null,\"message\":\"Asset holdings has been retrieved.\",\"resultSet\":\"[{\\\"org_unit_id\\\":1,\\\"asset_id\\\":1,\\\"quantity\\\":1\\\"}]\",\"resultSetType\":\"json\",\"success\":\"1\"}",
                ServerLogicCommands.getAssetHoldings(true, db));
    }

    /**
     * Test for creating asset holdings
     */
    @Test
    public void testCreateAssetHoldingWhenNotLoggedIn() throws Exception {
        db = new AssetHoldingDbMock(null, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageAssetHolding(false, obj, db));
    }

    @Test
    public void testCreateAssetHoldingWithLoggedInvalid() throws Exception {
        db = new AssetHoldingDbMock(null, false, null);
        JSONAction obj = new JSONAction("Manage_Asset_Holding", "create", new AssetHoldingModel(1,1,1), "AssetHoldingModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":\"Asset Holding was NOT created.\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageAssetHolding(true, obj, db));
    }

    @Test
    public void testCreateAssetHoldingWithLoggedInAndValidResult() throws Exception {
        db = new AssetHoldingDbMock(null, true, null);
        JSONAction obj = new JSONAction("Manage_Asset_Holding", "create", new AssetHoldingModel(1,1,1), "AssetHoldingModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":null,\"message\":\"Asset Holding was created.\",\"success\":\"1\"}";
        assertEquals(expected, ServerLogicCommands.manageAssetHolding(true, obj, db));
    }

    /**
     * Test for updating asset holdings
     */
    @Test
    public void testUpdatingAssetHoldingWhenNotLoggedIn() throws Exception {
        db = new AssetHoldingDbMock(null, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageAssetHolding(false, obj, db));
    }

    @Test
    public void testUpdatingAssetHoldingWithLoggedInvalid() throws Exception {
        db = new AssetHoldingDbMock(null, null, false);
        JSONAction obj = new JSONAction("Manage_Asset_Holding", "update", new AssetHoldingModel(1,1,1), "AssetHoldingModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":\"Asset Holding was NOT updated\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageAssetHolding(true, obj, db));
    }

    @Test
    public void testUpdatingAssetHoldingWithLoggedInAndValidResult() throws Exception {
        db = new AssetHoldingDbMock(null, null, true);
        JSONAction obj = new JSONAction("Manage_Asset_Holding", "update", new AssetHoldingModel(1,1,1), "AssetHoldingModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        String expected = "{\"errorMessage\":null,\"message\":\"Asset Holding was updated.\",\"success\":\"1\"}";
        assertEquals(expected, ServerLogicCommands.manageAssetHolding(true, obj, db));
    }

}
