package server.dal;

public class TradeHistoryDbMock implements TradeHistoryDb {

    private String getTradeHistoryReturn;
    private int getCountOfTradesForAssetReturn;
    private String getTradeHistoryByAssetReturn;
    private String getLatestTradeMessageReturn;

    public TradeHistoryDbMock(String getTradeHistoryReturn, int getCountOfTradesForAssetReturn, String getTradeHistoryByAssetReturn, String getLatestTradeMessageReturn) {
        this.getTradeHistoryReturn = getTradeHistoryReturn;
        this.getCountOfTradesForAssetReturn = getCountOfTradesForAssetReturn;
        this.getTradeHistoryByAssetReturn = getTradeHistoryByAssetReturn;
        this.getLatestTradeMessageReturn = getLatestTradeMessageReturn;
    }

    @Override
    public String getTradeHistory() {
        return getTradeHistoryReturn;
    }

    @Override
    public int getCountOfTradesForAsset(String assetName) {
        return getCountOfTradesForAssetReturn;
    }

    @Override
    public String getTradeHistory(String assetName) {
        return getTradeHistoryByAssetReturn;
    }

    @Override
    public String getLatestTradeMessage(String tradeType, String orgUnitName) {
        return getLatestTradeMessageReturn;
    }
}
