package AssetTradingTest.common.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class TradeHistoryModel {
    private int trade_id_sell;
    private int trade_id_buy;
    private String trade_type;
    private String org_unit_name;
    private String username;
    private String asset_name;
    private int quantity;
    private int price;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Australia/Brisbane") // needed for Jackson to parse the sql Timestamp variable
    private Timestamp trade_date;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Australia/Brisbane")
    private Timestamp date_processed;

    TradeHistoryModel() { }

    public TradeHistoryModel(int trade_id_sell, int trade_id_buy, String trade_type,
                             String org_unit_name, String username, String asset_name,
                             int quantity, int price, Timestamp trade_date,
                             Timestamp date_processed) {
        this.trade_id_sell = trade_id_sell;
        this.trade_id_buy = trade_id_buy;
        this.trade_type = trade_type;
        this.org_unit_name = org_unit_name;
        this.username = username;
        this.asset_name = asset_name;
        this.quantity = quantity;
        this.price = price;
        this.trade_date = trade_date;
        this.date_processed = date_processed;
    }

    public int getTrade_id_sell() {
        return trade_id_sell;
    }

    public void setTrade_id_sell(int trade_id_sell) {
        this.trade_id_sell = trade_id_sell;
    }

    public int getTrade_id_buy() {
        return trade_id_buy;
    }

    public void setTrade_id_buy(int trade_id_buy) {
        this.trade_id_buy = trade_id_buy;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getOrg_unit_name() {
        return org_unit_name;
    }

    public void setOrg_unit_name(String org_unit_name) {
        this.org_unit_name = org_unit_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public void setAsset_name(String asset_name) {
        this.asset_name = asset_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(Timestamp trade_date) {
        this.trade_date = trade_date;
    }

    public Timestamp getDate_processed() {
        return date_processed;
    }

    public void setDate_processed(Timestamp date_processed) {
        this.date_processed = date_processed;
    }

    @Override
    public String toString() {
        return "[TradeHistoryModel{]" +
                "trade_id_sell=" + trade_id_sell +
                ", trade_id_buy=" + trade_id_buy +
                ", trade_type='" + trade_type + '\'' +
                ", org_unit_name='" + org_unit_name + '\'' +
                ", username='" + username + '\'' +
                ", asset_name='" + asset_name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", trade_date=" + trade_date +
                ", date_processed=" + date_processed +
                ']';
    }
}
