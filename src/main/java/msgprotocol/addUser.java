package msgprotocol;

public class addUser implements sqlQuery {
    /**
     * Add users - Username, Password, Account_type, and OU.
     *
     */

    String user_name;
    String password;
    int account_type_id;
    int org_unit_id;

    public addUser(String user_name, String password, int account_type_id, int org_unit_id){
        this.user_name = user_name;
        this.password = password;
        this.account_type_id = account_type_id;
        this.org_unit_id = org_unit_id;
    }

    @Override
    public String SQL_query() {
        return "INSERT INTO user (username, password, account_type_id, org_unit_id)"
                + "VALUES ("+user_name+", "+password+", "+account_type_id+","+org_unit_id+")";
    }
}
