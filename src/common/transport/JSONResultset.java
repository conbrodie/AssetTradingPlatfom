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

public class JSONResultset {
    private String success;
    private String message;
    private String error_message;
    private String result_set_type;
    private String result_set;

    public JSONResultset() {  }

    public JSONResultset(String success, String message, String error_message,
                                         String result_set_type, String result_set) {
        this.success = success;
        this.message = message;
        this.error_message = error_message;
        this.result_set_type = result_set_type;
        this.result_set = result_set;
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

    public String getErrorMessage() {
        return error_message;
    }

    public void setErrorMessage(String error_message) {
        this.error_message = error_message;
    }

    public String getResultSetType() {
        return result_set_type;
    }

    public void setResultSetType(String result_set_type) {
        this.result_set_type = result_set_type;
    }

    public String getResultSet() {
        return result_set;
    }

    public void setResultSet(String result_set) {
        this.result_set = result_set;
    }

}