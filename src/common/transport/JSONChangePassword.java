package common.transport;

/**
 *     This class is used to assist in transportation of information between the client and server.
 *
 *     Information is added to the object instantiated from this class and then it is transformed (serialised)
 *     into a JSON formatted string.
 *     This JSON string is transferred over the network, when is arrives at its destination it is (de-serialised)
 *     and used to recreate the object from the JSON string. The contents of this object are then used to
 *     supply the relevant values to the client or the server.
 *
 *     It is the in-between class in the transportation process between the client and server.
 */

public class JSONChangePassword {
    private String username;
    private String oldPassword;
    private String newPassword;

    public JSONChangePassword() { };

    public JSONChangePassword(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
