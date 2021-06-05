package server.dal;

public interface TradeHistoryDb {
    String getTradeHistory();
    int getCountOfTradesForAsset(String assetName);
    String getTradeHistory(String assetName);
    String getLatestTradeMessage(String tradeType, String orgUnitName);
}
