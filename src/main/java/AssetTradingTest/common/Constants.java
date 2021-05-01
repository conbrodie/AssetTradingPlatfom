package AssetTradingTest.common;

public class Constants {
    /**
     * Used to hold the application's constants.
     */

    // Constant used to hold the size of message header
    public final static  int HEADER_SIZE = 4; // bytes

    // Constant used to hold table type
    public static enum TABLE_TYPE { trade, empty };

    // Enumerated ordinals - manage message types sent by clients
    public static enum MessageType  { Check, LogIn, Action, Reconcile_Message };

    // Enumerated ordinals - one for each action command sent by client
    public static enum Action {  Manage_User, Manage_Org_Unit, Manage_Asset_Holding, Create_Asset,
        Add_Trade, Delete_Trade, Change_Password, Get_Current_Trades, Get_Graph_History, Get_Users,
        Get_Assets, Get_Asset_Holdings, Get_Org_Units, Get_Accounts };

}
