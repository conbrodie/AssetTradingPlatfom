package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.Constants;
import common.transport.JSONAction;
import server.dal.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to support the ServerThread class and allow the Action commands to be executed. This class is
 * the only place that the database is accessed.
 * The Action message is found using its action command name which is a enumerated ordinal defined in the
 * Constants class.
 *
 * The action command is found and the necessary steps are taken to retrieve the information from the database. The
 * result of this then packaged in a class object, this object is then serialised into formatted text string and
 * sent to the client.
 *
 * The client then de-serialises it into the same class object and its contents are used to show the user the results
 * of the action command they originated.
 *
 * So data is only passed back in a structured text format - i.e. json.
 */

public class ServerLogic {
    private final Logger LOGGER;
    private boolean validPassword = false;

    /**
     * Constructor
     * @param logger used to log messages
     */
    public ServerLogic(Logger logger ) { this.LOGGER = logger; }

    /**
     *
     * @param obj JSONAction object containing the action values. In particular the Action type i.e. Manage_User
     * @param username username of the user requesting the action
     * @return Json formatted string, dependent on the requested command
     * @throws JsonProcessingException If parsing is not correctly completed this exception is thrown
     */
    public String ProcessCommand(JSONAction obj, String username) throws JsonProcessingException {
        Constants.Action CmdType = Constants.Action.valueOf(obj.getCommand()); // used to find the correct command/action
        switch (CmdType) {
            case LogIn -> {
                ServerLogicCommands.LoginResult result = ServerLogicCommands.login(username, obj, new User());
                validPassword = result.validPassword;
                if (result.validPassword) {
                    LOGGER.log(Level.INFO, "User '" + result.username + "' has signed in successfully");
                }
                return result.message;
            }
            case Manage_User -> {
                return ServerLogicCommands.manageUser(validPassword, obj, new User());
            }
            case Manage_Org_Unit -> {
                return ServerLogicCommands.manageOrgUnit(validPassword, obj, new OrgUnit());
            }
            case Manage_Asset_Holding -> {
                return ServerLogicCommands.manageAssetHolding(validPassword, obj, new AssetHolding());
            }
            case Create_Asset -> {
                return ServerLogicCommands.createAsset(validPassword, obj, new Asset());
            }
            case Change_Password -> {
                return ServerLogicCommands.changePassword(validPassword, obj, new User());
            }
            case Add_Trade -> {
                return ServerLogicCommands.addTrade(validPassword, obj, new TradeCurrent());
            }
            case Delete_Trade -> {
                return ServerLogicCommands.deleteTrade(validPassword, obj, new TradeCurrent());
            }
            case Get_Graph_History -> {
                return ServerLogicCommands.getGraphHistory(validPassword, obj, new TradeHistory());
            }
            case Get_Users -> {
                return ServerLogicCommands.getUsers(validPassword, new User());
            }
            case Get_Assets -> {
                return ServerLogicCommands.getAssets(validPassword, new Asset());
            }
            case Get_Asset_Holdings -> {
                return ServerLogicCommands.getAssetHoldings(validPassword, new AssetHolding());
            }
            case Get_Org_Units -> {
                return ServerLogicCommands.getOrgUnits(validPassword, new OrgUnit());
            }
            case Get_Accounts -> {
                return ServerLogicCommands.getAccounts(validPassword, new AccountType());
            }
            case Get_Current_Trades -> {
                return ServerLogicCommands.getCurrentTrades(validPassword, new TradeCurrent());
            }
            case Get_Latest_Trade_Message -> {
                return ServerLogicCommands.getLatestTradeMessage(validPassword, obj, new TradeHistory());
            }
            default -> {
                LOGGER.log(Level.INFO, "Server:Logic - Command: " + CmdType.name() + " unknown.");
                return "";
            }
        }
    }
}
