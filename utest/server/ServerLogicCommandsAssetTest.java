package server;

import static  org.junit.jupiter.api.Assertions.*;

import common.transport.JSONAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dal.AssetDbMock;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;

public class ServerLogicCommandsAssetTest {
    private AssetDbMock db;

    @BeforeEach
    public void setup() throws CredentialException {
    }
    /**
     * Test for get assets method
     */
    @Test
    public void testGetAssetsWhenNotLoggedIn() throws Exception {
        db = new AssetDbMock(new ArrayList<>(), new ArrayList<>());
        String expected = "{\"success\":\"0\",\"message\":null," +
                "\"errorMessage\":\"Password was not valid, Are you logged in!\"," +
                "\"resultSetType\":null,\"resultSet\":null}";
        assertEquals(expected, ServerLogicCommands.getAssets(false, db));
    }


    /**
     * Test for create assets method
     */
    @Test
    public void testCreateAssetWhenNotLoggedIn() throws Exception {
        db = new AssetDbMock(new ArrayList<>(), new ArrayList<>());
        JSONAction obj = new JSONAction();
        assertEquals("{\"success\":\"0\",\"message\":null," +
                "\"errorMessage\":\"Password was not valid, Are you logged in!\"}",
                ServerLogicCommands.createAsset(false, obj, db));
    }

}
