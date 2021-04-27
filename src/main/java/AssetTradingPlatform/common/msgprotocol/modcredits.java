package AssetTradingPlatform.common.msgprotocol;

public class modcredits implements sqlQuery {

    String org_unit_name;
    int credit_amount;

    public modcredits(String org_unit_name, int credit_amount){
        this.org_unit_name = org_unit_name;
        this.credit_amount = credit_amount;

    }

    @Override
    public String SQL_query() {
        return "UPDATE user "
                +"SET credits ="+credit_amount+""
                +"WHERE org_unit ='"+org_unit_name+"'";

    }
}
