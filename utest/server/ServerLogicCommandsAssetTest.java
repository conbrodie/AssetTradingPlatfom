package server;

import static  org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.models.AssetModel;
import common.transport.JSONAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dal.AccountTypeDbMock;
import server.dal.AssetDbMock;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;

public class ServerLogicCommandsAssetTest {
    private AssetDbMock db;
    private static String DB_FAIL = "{\"errorMessage\":\"Error when trying to retrieve assets.\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";

    /**
     * Test for get assets method
     */
    @Test
    public void testGetAssetsWhenNotLoggedIn() throws Exception {
        db = new AssetDbMock(null, false);
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getAssets(false, db));
    }

    @Test
    public void testGetAssetsWithLoggedInAndNull() throws JsonProcessingException {
        db = new AssetDbMock(null, false);
        assertEquals(DB_FAIL,
                ServerLogicCommands.getAssets(true, db));
    }
    @Test
    public void testGetAssetsWithLoggedInAndEmpty() throws JsonProcessingException {
        db = new AssetDbMock("", false);
        assertEquals(DB_FAIL,
                ServerLogicCommands.getAssets(true, db));
    }
    @Test
    public void testGetAssetsWithLoggedInAndValidResult() throws JsonProcessingException {
        String result = "[{\"asset_id\":1,\"asset_name\":\"CPU Hours\"}]";
        db = new AssetDbMock(result, false);
        assertEquals("{\"errorMessage\":null,\"message\":\"Assets has been retrieved.\",\"resultSet\":\"[{\\\"asset_id\\\":1,\\\"asset_name\\\":\\\"CPU Hours\\\"}]\",\"resultSetType\":\"json\",\"success\":\"1\"}",
                ServerLogicCommands.getAssets(true, db));
    }
    /**
     * Test for create assets method
     */
    @Test
    public void testCreateAssetWhenNotLoggedIn() throws Exception {
        db = new AssetDbMock(null, false);
        JSONAction obj = new JSONAction();
        assertEquals("{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\"}",
                ServerLogicCommands.createAsset(false, obj, db));
    }
    @Test
    public void testCreateAssetWhenLoggedInAndAssetInValid() throws Exception {
        db = new AssetDbMock(null, false);
        JSONAction obj = new JSONAction("Create_Asset", "create", new AssetModel(1, "CPU Hours"), "AssetModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        assertEquals("{\"errorMessage\":\"Asset 'CPU Hours' was not created.\",\"message\":null,\"success\":\"0\"}",
                ServerLogicCommands.createAsset(true, obj, db));
    }
    @Test
    public void testCreateAssetWhenLoggedInAndAssetValid() throws Exception {
        db = new AssetDbMock(null, true);
        JSONAction obj = new JSONAction("Create_Asset", "create", new AssetModel(1, "CPU Hours"), "AssetModel", "Compute Cluster Division", "CPU Hours", 2, "bob");
        assertEquals("{\"errorMessage\":null,\"message\":\"Asset 'CPU Hours' was created.\",\"success\":\"1\"}",
                ServerLogicCommands.createAsset(true, obj, db));
    }
}
