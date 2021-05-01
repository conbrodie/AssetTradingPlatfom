package AssetTradingTest.common.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class TradeCurrentModel {

    private int trade_id;
    private String trade_type;
    private int org_unit_id;
    private String org_unit_name;
    private int user_id;
    private String username;
    private int asset_id;
    private String asset_name;
    private int quantity;
    private int price;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Australia/Brisbane") // needed for Jackson to parse the sql Timestamp variable
    private Timestamp trade_date;

    public TradeCurrentModel() { }

    public TradeCurrentModel(int trade_id, String trade_type, int org_unit_id, String org_unit_name,
                             int user_id, String username, int asset_id, String asset_name,
                             int quantity, int price, Timestamp trade_date) {
        this.trade_id = trade_id;
        this.trade_type = trade_type;
        this.org_unit_id = org_unit_id;
        this.org_unit_name = org_unit_name;
        this.user_id = user_id;
        this.username = username;
        this.asset_id = asset_id;
        this.asset_name = asset_name;
        this.quantity = quantity;
        this.price = price;
        this.trade_date = trade_date;
    }

    public int getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(int trade_id) {
        this.trade_id = trade_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public int getOrg_unit_id() {
        return org_unit_id;
    }

    public void setOrg_unit_id(int org_unit_id) {
        this.org_unit_id = org_unit_id;
    }

    public String getOrg_unit_name() {
        return org_unit_name;
    }

    public void setOrg_unit_name(String org_unit_name) {
        this.org_unit_name = org_unit_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(int asset_id) {
        this.asset_id = asset_id;
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

    @Override
    public String toString() {
        return "[" +
                "trade_id=" + trade_id +
                ", trade_type='" + trade_type + '\'' +
                ", org_unit_id=" + org_unit_id +
                ", org_unit_name='" + org_unit_name + '\'' +
                ", user_id=" + user_id +
                ", username='" + username + '\'' +
                ", asset_id=" + asset_id +
                ", asset_name='" + asset_name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", trade_date=" + trade_date +
                ']';
    }

}
