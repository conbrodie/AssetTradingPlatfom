package AssetTradingPlatform.common.msgprotocol;

public class modasset implements sqlQuery {

    int org_unit_id;
    int asset_id;
    int quantity;

    public modasset(int org_unit_id, int asset_id, int credit_amount){
        this.org_unit_id = org_unit_id;
        this.asset_id = asset_id;
        this.quantity = quantity;

    }

    @Override
    public String SQL_query() {
        return "UPDATE user "
                +"SET quantity ="+quantity+""
                +"WHERE org_unit_id ='"+org_unit_id+"and asset_id ='"+asset_id+"'";

    }
}
