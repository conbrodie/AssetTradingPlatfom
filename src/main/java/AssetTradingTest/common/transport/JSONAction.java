package AssetTradingTest.common.transport;

public class JSONAction {

    private String command;
    private String sqlStatementType; // select, create, update, delete ??
    private Object object;
    private String objectType;
    private String assetName;
    private int id;
    private String username;

    public JSONAction() { }

    public JSONAction(String command, String sqlStatementType, Object object,
                      String objectType, String assetName, int id, String username) {
        this.command = command;
        this.sqlStatementType = sqlStatementType;
        this.object = object;
        this.objectType = objectType;
        this.assetName = assetName;
        this.id = id;
        this.username = username;
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

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getId() {
        return id;
    }

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
