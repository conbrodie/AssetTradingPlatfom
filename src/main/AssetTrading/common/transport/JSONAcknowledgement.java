package AssetTrading.common.transport;

public class JSONAcknowledgement {

    private String success;
    private String message;
    private String errorMessage;

    public JSONAcknowledgement() { }

    public JSONAcknowledgement(String success, String message, String errorMessage) {
        this.success = success;
        this.message = message;
        this.errorMessage = errorMessage;
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
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
