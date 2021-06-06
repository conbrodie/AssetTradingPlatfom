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

public class JSONAction {
    private String command;
    private String sqlStatementType; // select, create, update, delete ??
    private Object object;
    private String objectType;
    private String orgUnitName;
    private String assetName;
    private int id;
    private String username;

    public JSONAction() { }

    public JSONAction(String command, String sqlStatementType, Object object,
                      String objectType, String orgUnitName, String assetName,
                      int id, String username) {
        this.command = command;
        this.sqlStatementType = sqlStatementType;
        this.object = object;
        this.objectType = objectType;
        this.orgUnitName = orgUnitName;
        this.assetName = assetName;
        this.id = id;
        this.username = username;
    }

    public JSONAction(String command, String sqlStatementType, Object object,
                      String objectType) {
        this.command = command;
        this.sqlStatementType = sqlStatementType;
        this.object = object;
        this.objectType = objectType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSqlStatementType() {
        return sqlStatementType;
    }

    public void setSqlStatementType(String sqlStatementType) {
        this.sqlStatementType = sqlStatementType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
