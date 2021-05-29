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

public class JSONAddTradeAcknowledgement extends JSONAcknowledgement {
    private int tradeId;

    public JSONAddTradeAcknowledgement() {
        super();
    }

    public JSONAddTradeAcknowledgement(String success, String message, String errorMessage, int tradeId) {
        super(success, message, errorMessage);
        this.tradeId = tradeId;
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }
}
