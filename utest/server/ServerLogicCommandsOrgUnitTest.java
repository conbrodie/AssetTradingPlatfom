package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.models.OrgUnitModel;
import common.transport.JSONAction;
import org.junit.jupiter.api.Test;
import server.dal.AssetHoldingDbMock;
import server.dal.OrgUnitDbMock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerLogicCommandsOrgUnitTest {
    private OrgUnitDbMock db;
    /**
     * Tests for getting Organisation Units
     */
    @Test
    public void testGetOrgUnitsWhenNotLoggedIn() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, null, null);
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getOrgUnits(false, db));
    }

    @Test
    public void testGetOrgUnitWithLoggedInAnNull() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, null, null);
        String expected = "{\"errorMessage\":\"Error when trying to retrieve organisational units.\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getOrgUnits(true, db));
    }

    @Test
    public void testGetOrgUnitWithLoggedInAnEmpty() throws JsonProcessingException {
        db = new OrgUnitDbMock("", null, null);
        String expected = "{\"errorMessage\":\"Error when trying to retrieve organisational units.\",\"message\":null,\"resultSet\":null,\"resultSetType\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.getOrgUnits(true, db));
    }

    @Test
    public void testGetOrgUnitWithLoggedInAndValidResult() throws JsonProcessingException {
        db = new OrgUnitDbMock("[{\"org_unit_id\":1,\"asset_id\":1,\"quantity\":1\"}]", null, null);
        assertEquals("{\"errorMessage\":null,\"message\":\"Organisational units has been retrieved.\",\"resultSet\":\"[{\\\"org_unit_id\\\":1,\\\"asset_id\\\":1,\\\"quantity\\\":1\\\"}]\",\"resultSetType\":\"json\",\"success\":\"1\"}",
                ServerLogicCommands.getOrgUnits(true, db));
    }

    /**
     * Tests for creating Organisation Units
     */

    @Test
    public void testCreateOrgUnitWhenNotLoggedIn() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageOrgUnit(false, obj, db));
    }

    @Test
    public void testCreateOrgUnitWithLoggedInAnInvalid() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, false, null);
        JSONAction obj = new JSONAction("Manage_Org_Unit", "create", new OrgUnitModel("Compute Cluster Division", 200), "OrgUnitModel", null, null, 2, "bob");
        String expected = "{\"errorMessage\":\"Organisation 'Compute Cluster Division' was not created.\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageOrgUnit(true, obj, db));
    }

    @Test
    public void testCreateOrgUnitWithLoggedInAndValidResult() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, true, null);
        JSONAction obj = new JSONAction("Manage_Org_Unit", "create", new OrgUnitModel("Compute Cluster Division", 200), "OrgUnitModel", null, null, 2, "bob");
        assertEquals("{\"errorMessage\":null,\"message\":\"Organisation 'Compute Cluster Division' was created.\",\"success\":\"1\"}",
                ServerLogicCommands.manageOrgUnit(true, obj, db));
    }

    /**
     * Tests for updating Organisation Units
     */

    @Test
    public void testUpdateOrgUnitWhenNotLoggedIn() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, null, null);
        JSONAction obj = new JSONAction();
        String expected = "{\"errorMessage\":\"Password was not valid, Are you logged in!\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageOrgUnit(false, obj, db));
    }

    @Test
    public void testUpdateOrgUnitWithLoggedInAnInvalid() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, null, false);
        JSONAction obj = new JSONAction("Manage_Org_Unit", "update", new OrgUnitModel("Compute Cluster Division", 200), "OrgUnitModel", null, null, 2, "bob");
        String expected = "{\"errorMessage\":\"Organisation 'Compute Cluster Division' was not updated\",\"message\":null,\"success\":\"0\"}";
        assertEquals(expected, ServerLogicCommands.manageOrgUnit(true, obj, db));
    }

    @Test
    public void testUpdateOrgUnitWithLoggedInAndValidResult() throws JsonProcessingException {
        db = new OrgUnitDbMock(null, null, true);
        JSONAction obj = new JSONAction("Manage_Org_Unit", "update", new OrgUnitModel("Compute Cluster Division", 200), "OrgUnitModel", null, null, 2, "bob");
        assertEquals("{\"errorMessage\":null,\"message\":\"Organisation 'Compute Cluster Division' was updated.\",\"success\":\"1\"}",
                ServerLogicCommands.manageOrgUnit(true, obj, db));
    }
}
