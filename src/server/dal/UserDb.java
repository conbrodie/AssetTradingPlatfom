package server.dal;

public interface UserDb {
    String getUserSecurityCredentials(String username);
    int getUserId(String username);
    String getUsers();
    String getUserDetails(String username);
    String getUsernames();
    boolean createUser(String username, String password, int org_unit_id, int account_type_id);
    boolean updateUser(int user_id, String username, String password, int org_unit_id, int account_type_id);
    boolean changePassword(String username, String newPassword, String oldPassword);
}
