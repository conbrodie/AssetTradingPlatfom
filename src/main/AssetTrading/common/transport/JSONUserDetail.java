package AssetTrading.common.transport;

public class JSONUserDetail {
    private int user_id;
    private String username;
    private int org_unit_id;
    private String org_unit_name;
    private int account_type_id;
    private String account_type;

    public JSONUserDetail() { }

    public JSONUserDetail(int user_id, String username, int org_unit_id, String org_unit_name, int account_type_id, String account_type) {
        this.user_id = user_id;
        this.username = username;
        this.org_unit_id = org_unit_id;
        this.org_unit_name = org_unit_name;
        this.account_type_id = account_type_id;
        this.account_type = account_type;
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

    public int getAccount_type_id() {
        return account_type_id;
    }

    public void setAccount_type_id(int account_type_id) {
        this.account_type_id = account_type_id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}
