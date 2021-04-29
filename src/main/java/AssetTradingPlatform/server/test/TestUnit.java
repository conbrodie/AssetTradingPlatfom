package AssetTradingPlatform.server.test;

import AssetTradingPlatform.common.Asset;
import AssetTradingPlatform.common.Unit;
import AssetTradingPlatform.server.UnitController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.CredentialException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals( new Unit("HR Management", 1500), b,"Failed to create an Organisational Unit");
    }
    @Test
    public void  testSpecialCharacterInUnitName() throws Exception {
        Unit b = unit.createUnit("HR@Management", 1500);
        assertEquals( new Unit("HR@Management", 1500), b,"Failed to create an Organisational Unit");
    }
    @Test
    public void  testMinStringLengthUnitName() throws Exception {
        Unit b = unit.createUnit("H", 1);
        assertEquals( new Unit("H", 1), b,"Failed to create an Organisational Unit");
    }
    @Test
    public void  testMaxStringLengthUnitName() throws Exception {
        Unit b = unit.createUnit("aaaaaaaaaaaaaaaa", 1500);
        assertEquals( new Unit("H", 1500), b,"Failed to create an Organisational Unit");
    }
    @Test
    public void  testIfCreditsIsTooBig() throws Exception {
        Unit b = unit.createUnit("HR Management", 2147483647);
        assertEquals( new Unit("HR Management",2147483647), b,"Failed to create an Organisational Unit");
    }
    @Test
    public void testLeaveUnitNameBlank(){
        assertThrows(CredentialException.class, () ->{
            unit.createUnit("", 1500);
        });
    }
    @Test
    public void testLeaveCreditsZero(){
        assertThrows(CredentialException.class, () ->{
            unit.createUnit("HR Management", 0);
        });
    }
    @Test
    public void testCreditsIsNegativeNumber(){
        assertThrows(CredentialException.class, () ->{
            unit.createUnit("HR Management", -10);
        });
    }
    @Test
    public void testUnitNameIsTooLong(){
        assertThrows(CredentialException.class, () ->{
            unit.createUnit("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 1500);
        });
    }
    @Test
    public void testInvalidUnitName(){
        assertThrows(CredentialException.class, () ->{
            unit.createUnit("HR:&#/", 1500);
        });
    }


}
