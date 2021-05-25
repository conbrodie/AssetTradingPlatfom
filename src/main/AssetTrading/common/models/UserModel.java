package AssetTrading.common.models;

public class UserModel {
    private int user_id;
    private String username;
    private String password;
    private int account_type_id;
    private int org_unit_id;

    public UserModel() { }

    public UserModel(int user_id, String username, String password, int account_type_id, int org_unit_id) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.account_type_id = account_type_id;
        this.org_unit_id = org_unit_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccount_type_id() {
        return account_type_id;
    }

    public void setAccount_type_id(int account_type_id) {
        this.account_type_id = account_type_id;
    }

    public int getOrg_unit_id() {
        return org_unit_id;
    }

    public void setOrg_unit_id(int org_unit_id) {
        this.org_unit_id = org_unit_id;
    }

    @Override
    public String toString() {
        return "[UserModel{]" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", account_type_id=" + account_type_id +
                ", org_unit_id=" + org_unit_id +
                ']';
    }
}
