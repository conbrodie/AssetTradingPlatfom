package client.gui;

import client.TradeTableModel;
import common.Utilities;
import common.models.TradeCurrentModel;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TradeTableModelTest {
    @Test
    public void testAddTrade(){
        TradeTableModel ttm = new TradeTableModel(new ArrayList<>());
        Timestamp time = new Timestamp(1622873054735l);
        ttm.addTrade(new TradeCurrentModel(1, "BUY", 1, "org", 10, "user", 3, "asset", 5, 20, time));
        assertEquals(1, ttm.getRowCount());
        assertEquals("org", ttm.getValueAt(0, 1));
        assertEquals("user", ttm.getValueAt(0, 2));
        assertEquals("asset", ttm.getValueAt(0, 3));
        assertEquals(5, ttm.getValueAt(0, 4));
        assertEquals(20, ttm.getValueAt(0, 5));
        assertEquals("2021-06-05 16:04:14", ttm.getValueAt(0, 6));
        assertEquals(1, ttm.getValueAt(0, 7));

    }
}
