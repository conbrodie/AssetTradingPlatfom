package AssetTradingPlatform.server.test;

import AssetTradingPlatform.common.Asset;
import AssetTradingPlatform.common.Order;
import AssetTradingPlatform.common.OrderRequest;
import AssetTradingPlatform.common.Unit;
import AssetTradingPlatform.server.OrderController;
import AssetTradingPlatform.server.UnitController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.CredentialException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUnit {
    private AssetTradingPlatform.server.DB DB;
    private UnitController unit;
    @BeforeEach
    public void setup() throws CredentialException {
        DB = new MockDB();
        unit = new UnitController(DB);
    }
    /**
     * Test for createUnit method
     */
    @Test
    public void  testSuccessCreateOrgUnit() throws Exception {
        Unit b = unit.createUnit("HR Management", 1500);
        assertEquals( new Unit("HR Management", 1500, Collections.emptyMap()), b,"Failed to create an Organisational Unit");
    }
}
