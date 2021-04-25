package AssetTradingPlatform.server.test;

import AssetTradingPlatform.common.Asset;
import AssetTradingPlatform.server.AssetController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.CredentialException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAsset {
    private AssetTradingPlatform.server.DB DB;
    private AssetController asset;
    @BeforeEach
    public void setup(){
        DB = new MockDB();
        asset = new AssetController(DB);
    }
    /**
     * Test for createAsset Method
     */
    @Test
    public void testSuccessToCreateAsset(){
        Asset a = asset.createAsset("CPU hours", 10, 500, "ComputeClusterDivision" );
        assertEquals(new Asset("CPU hours"), a, "Failed to create an asset");
    }
    @Test
    public void testMaxLengthStringAssetName(){
        Asset a = asset.createAsset("kkkkkkkkkkkkkkkk", 10, 500, "ComputeClusterDivision" );
        assertEquals(new Asset("kkkkkkkkkkkkkkkk"), a, "Failed to create an asset");
    }
    @Test
    public void testMinLengthStringAssetName(){
        Asset a = asset.createAsset("a", 10, 500, "ComputeClusterDivision" );
        assertEquals(new Asset("a"), a, "Failed to create an asset");
    }
    @Test
    public void testIfPriceIsABigNumber(){
        Asset a = asset.createAsset("CPU hours", 2147483647, 500, "ComputeClusterDivision" );
        assertEquals(new Asset("CPU hours"), a, "Failed to create an asset");
    }
    @Test
    public void testIfQuantityIsBigNumber(){
        Asset a = asset.createAsset("CPU hours", 10, 2147483647, "ComputeClusterDivision" );
        assertEquals(new Asset("CPU hours"), a, "Failed to create an asset");
    }
    @Test
    public void testIfPriceEqualWithQuantity(){
        Asset a = asset.createAsset("CPU hours", 500, 500, "ComputeClusterDivision" );
        assertEquals(new Asset("CPU hours"), a, "Failed to create an asset");
    }
    @Test
    public void testIfAssetNameIsANumber(){
        Asset a = asset.createAsset("-10", 10, 2147483647, "ComputeClusterDivision" );
        assertEquals(new Asset("-10"), a, "Failed to create an asset");
    }
    @Test
    public void testAssetNameIsTooLong() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 10, 500, "ComputeClusterDivision");
        });
    }
    @Test
    public void testLeaveAssetNameBlank() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("", 10, 500, "ComputeClusterDivision");
        });
    }
    @Test
    public void testLeaveUnitNameBlank() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("", 10, 500, "");
        });
    }
    @Test
    public void testPriceIsZero() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("CPU hours", 0, 500, "ComputeClusterDivision");
        });
    }
    @Test
    public void testQuantityIsZero() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("CPU hours", 10, 0, "ComputeClusterDivision");
        });
    }
    @Test
    public void testPrizeIsNegativeNumber() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("CPU hours", -10, 500, "ComputeClusterDivision");
        });
    }
    @Test
    public void testQuantityIsNegativeNumber() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("CPU hours", 10, -500, "ComputeClusterDivision");
        });
    }
    @Test
    public void testInvalidAssetName() {
        assertThrows(CredentialException.class, () -> {
            asset.createAsset("CPU>//hours", 10, -500, "ComputeClusterDivision");
        });
    }
}
