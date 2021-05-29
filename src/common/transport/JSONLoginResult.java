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

public class JSONLoginResult {
    private String success;
    private String message;
    private String password;
    private String jsonUserDetails;
    private String errorMessage;
    private String clientName;

    public JSONLoginResult() { }

    public JSONLoginResult(String success, String message, String password, String details,
                           String errorMessage, String clientName) {
        this.success = success;
        this.message = message;
        this.password = password;
        this.jsonUserDetails = details;
        this.errorMessage = errorMessage;
        this.clientName = clientName;
    }

    public JSONLoginResult(JSONLoginResult obj) {
        this.success = obj.success;
        this.message = obj.message;
        this.password = obj.password;
        this.jsonUserDetails = obj.jsonUserDetails;
        this.errorMessage = obj.errorMessage;
        this.clientName = obj.clientName;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJsonUserDetails() {
        return jsonUserDetails;
    }

    public void setJsonUserDetails(String jsonUserDetails) {
        this.jsonUserDetails = jsonUserDetails;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
