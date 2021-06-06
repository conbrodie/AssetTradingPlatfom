package server.dal;

public class UserDbMock implements UserDb {

    private Boolean createUserReturn;
    private String getUsersReturn;
    private int getUserIdReturn;

    public UserDbMock(Boolean createUserReturn, String getUsersReturn, int getUserIdReturn) {
        this.createUserReturn = createUserReturn;
        this.getUsersReturn = getUsersReturn;
        this.getUserIdReturn = getUserIdReturn;
    }

    @Override
    public String getUserSecurityCredentials(String username) {
        return null;
    }

    @Override
    public int getUserId(String username) {
        return getUserIdReturn;
    }

    @Override
    public String getUsers() {
        return getUsersReturn;
    }

    @Override
    public String getUserDetails(String username) {
        return null;
    }

    @Override
    public String getUsernames() {
        return null;
    }

    @Override
    public boolean createUser(String username, String password, int org_unit_id, int account_type_id) {
        return createUserReturn;
    }

    @Override
    public boolean updateUser(int user_id, String username, String password, int org_unit_id, int account_type_id) {
        return false;
    }

    @Override
    public boolean changePassword(String username, String newPassword, String oldPassword) {
        return false;
    }
}
