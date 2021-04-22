package AssetTradingPlatform.server.test;

import static  org.junit.jupiter.api.Assertions.*;

import AssetTradingPlatform.common.Order;
import AssetTradingPlatform.server.OrderController;
import AssetTradingPlatform.common.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.CredentialException;
import java.time.LocalDateTime;

public class TestOrder {
    private AssetTradingPlatform.server.DB DB;
    private OrderController order;
    @BeforeEach
    public void setup() throws CredentialException {
        DB = new MockDB();
        order = new OrderController(DB);
    }
    /**
     * Test for Create Buy Method
     */
    @Test
    public void  testSuccessCreateBuy() throws Exception {
        Order b = order.createOrder("CPU Hours", OrderRequest.BUY, 50, 10, "ComputeClusterDivision");
        assertEquals( new Order(1, LocalDateTime.now(), "CPU Hours", OrderRequest.BUY, 50, 10), b,"Failed to create a buy");
    }
    @Test
    public void  testBuyMinOfQuantityAndPrice() throws Exception {
        Order b = order.createOrder("CPU Hours", OrderRequest.BUY, 50, 10, "ComputeClusterDivision");
        assertEquals( new Order(1, LocalDateTime.now(), "CPU Hours", OrderRequest.BUY, 1, 1), b,"Failed to create a buy");
    }
    @Test
    public void  testBuyMaxOfQuantityAndPrice() throws Exception {
        Order b = order.createOrder("CPU Hours", OrderRequest.BUY, 50, 10, "ComputeClusterDivision");
        assertEquals( new Order(1, LocalDateTime.now(), "CPU Hours", OrderRequest.BUY, 2147483647, 2147483647), b,"Failed to create a buy");
    }
    @Test
    public void  testBuyPriceIsZero()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.BUY, 50, 0, "ComputeClusterDivision");
        });
    }
    @Test
    public void  testBuyQuantityIsZero()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.BUY, 0, 10, "ComputeClusterDivision");
        });
    }
    @Test
    public void  testBuyQuantityIsTooBig()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.BUY, 2147483648, 10, "ComputeClusterDivision");
        });
    }
    @Test
    public void  testBuyQuantityIsNegative()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.BUY, -10, 10, "ComputeClusterDivision");
        });
    }
    /**
     * Test for Create Sell Method
     */
    @Test
    public void  testSuccessCreateSell() throws Exception {
        Order b = order.createOrder("CPU Hours", OrderRequest.SELL, 50, 10, "ComputeClusterDivision");
        assertEquals( new Order(1, LocalDateTime.now(), "CPU Hours", OrderRequest.SELL, 50, 10), b,"Failed to create a sell");
    }
    @Test
    public void  testSellMinOfQuantityAndPrice() throws Exception {
        Order b = order.createOrder("CPU Hours", OrderRequest.SELL, 50, 10, "ComputeClusterDivision");
        assertEquals( new Order(1, LocalDateTime.now(), "CPU Hours", OrderRequest.SELL, 1, 1), b,"Failed to create a sell");
    }
    @Test
    public void  testSellMaxOfQuantityAndPrice() throws Exception {
        Order b = order.createOrder("CPU Hours", OrderRequest.SELL, 50, 10, "ComputeClusterDivision");
        assertEquals( new Order(1, LocalDateTime.now(), "CPU Hours", OrderRequest.SELL, 2147483647, 2147483647), b,"Failed to create a sell");
    }
    @Test
    public void  testSellPriceIsZero()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.SELL, 50, 0, "ComputeClusterDivision");
        });
    }
    @Test
    public void  testSellQuantityIsZero()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.SELL, 0, 10, "ComputeClusterDivision");
        });
    }
    @Test
    public void  testSellQuantityIsTooBig()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.SELL, 2147483648, 10, "ComputeClusterDivision");
        });
    }
//    @Test
//    public void  testSellAmountIsBiggerThanUnitHas()  {
//        assertThrows(CredentialException.class, () -> {
//
//            Order b = order.createOrder("CPU Hours", OrderRequest.SELL, 60, 10, "ComputeClusterDivision");
//        });
//    }
    @Test
    public void  testSellQuantityIsNegative()  {
        assertThrows(CredentialException.class, () -> {
            Order b = order.createOrder("CPU Hours", OrderRequest.SELL, -10, 10, "ComputeClusterDivision");
        });
    }

}