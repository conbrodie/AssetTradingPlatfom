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
     * Test for Create New Order Method
     */
    @Test
    public void  testSuccessCreateBuy() throws Exception {
        Order b = order.createOrder("CPU Hours", OrderRequest.BUY, 50, 10, "ComputeClusterDivision");
        assertEquals( new Order(1, LocalDateTime.now(), "CPU Hours", OrderRequest.BUY, 50, 10), b,"Failed to create a buy");
    }

}