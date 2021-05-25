package AssetTrading.common.transport;

public class JSONMessageType {

    private String messageType;

    public JSONMessageType() { };

    public JSONMessageType(String messageType) { this.messageType = messageType; }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

}