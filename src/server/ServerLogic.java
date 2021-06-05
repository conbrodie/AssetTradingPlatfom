package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.models.*;
import common.transport.*;
import server.dal.*;
import common.Constants;
import common.Utilities;

import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

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

    private Logger LOGGER;
    private final ObjectMapper objectMapper = new ObjectMapper();
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
     * @throws SQLException If database information retrieval is not correctly completed this exception is thrown
     */
    public String ProcessCommand(JSONAction obj, String username, String clientName) throws JsonProcessingException, SQLException {
        String cmd = obj.getCommand(); // command type eg. 'Manage_User'
        Constants.Action CmdType = Constants.Action.valueOf(obj.getCommand()); // used to find the correct command/action
        String jsonReturn = ""; // return string for all methods enclosed in 'switch statement'

        switch (CmdType) {
            case LogIn: {
                JSONLoginResult ogjLoginResult = new JSONLoginResult();

                // Need to convert to JSONLogin object due to Jackson serialisation as it
                // did not know enough about what the object was during the process.
                parseFromLinkedHashMapToObject(obj); // see comment above.
                JSONLogin login = (JSONLogin) obj.getObject();
                String userPassword = login.getPassword(); // hashed password from user to be tested
                // Get credentials from database.
                User user = new User();
                String retrievedPassword = user.getUserSecurityCredentials(login.getUsername());

                if (retrievedPassword != null) { // this will occur if user does not exist in db
                    if (userPassword.equals(retrievedPassword)) { // password is ok
                        validPassword = true;
                        String userDetails = user.getUserDetails(login.getUsername());
                        ogjLoginResult.setSuccess("1");
                        ogjLoginResult.setPassword(retrievedPassword);
                        ogjLoginResult.setJsonUserDetails(userDetails);
                        ogjLoginResult.setClientName(clientName);
                        ogjLoginResult.setMessage("User '" + login.getUsername() + "' is logged in!");
                        LOGGER.log(Level.INFO, "User '" + login.getUsername() + "' has signed in successfully");
                    }
                    else { // invalid
                        ogjLoginResult.setSuccess("0");
                        ogjLoginResult.setPassword("");
                        ogjLoginResult.setClientName(clientName);
                        ogjLoginResult.setErrorMessage("Please check username and password values!");
                    }
                }
                else { // username not in database
                    ogjLoginResult.setSuccess("0");
                    ogjLoginResult.setErrorMessage("Username unknown!");
                }
                jsonReturn = objectMapper.writeValueAsString(new JSONLoginResult(ogjLoginResult));

                break;
            }

            case Manage_User: {
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    User u = new User();
                    parseFromLinkedHashMapToObject(obj); // see comment above.
                    UserModel um = (UserModel) obj.getObject();
                    if (obj.getSqlStatementType().equals("create")) {
                        // Check if username already in User table - must be unique
                        if (u.getUserId(obj.getUsername()) <= 0) {
                            success = u.createUser(um.getUsername(), um.getPassword(), um.getOrg_unit_id(), um.getAccount_type_id());
                            if (success) {
                                objAcknowledgement.setMessage("User '" + um.getUsername() + "' was created.");
                            } else {
                                objAcknowledgement.setErrorMessage("User '" + um.getUsername() + "' was not created.");
                            }
                        }
                        else {
                            success = false;
                            objAcknowledgement.setErrorMessage("Username '" + um.getUsername() + "' is not available.");
                        }
                    }
                    else if (obj.getSqlStatementType().equals("update")) {
                        success = u.updateUser(um.getUser_id(), um.getUsername(), um.getPassword(), um.getOrg_unit_id(), um.getAccount_type_id());

                        if (success) {
                            objAcknowledgement.setMessage("User '" + um.getUsername() + "' was updated.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("User '" + um.getUsername() + "' was not updated");
                        }
                    }
                    objAcknowledgement.setSuccess(success ? "1" : "0");
                }
                else { // invalid password
                    objAcknowledgement.setSuccess("0");
                    objAcknowledgement.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objAcknowledgement);

                break;
            }

            case Manage_Org_Unit: {
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    OrgUnit ou = new OrgUnit();
                    parseFromLinkedHashMapToObject(obj);
                    OrgUnitModel oum = (OrgUnitModel) obj.getObject();
                    if (obj.getSqlStatementType().equals("create")) {
                        success = ou.createOrgUnit(oum.getOrg_unit_name(), oum.getCredits());
                        if (success) {
                            objAcknowledgement.setMessage("Organisation '" + oum.getOrg_unit_name() + "' was created.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("Organisation '" + oum.getOrg_unit_name() + "' was not created.");
                        }
                    }
                    else if (obj.getSqlStatementType().equals("update")) {
                        success = ou.updateOrgUnit(oum.getOrg_unit_id(), oum.getOrg_unit_name(), oum.getCredits());

                        if (success) {
                            objAcknowledgement.setMessage("Organisation '" + oum.getOrg_unit_name() + "' was updated.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("Organisation '" + oum.getOrg_unit_name() + "' was not updated");
                        }
                    }
                    objAcknowledgement.setSuccess(success ? "1" : "0");
                }
                else { // invalid password
                    objAcknowledgement.setSuccess("0");
                    objAcknowledgement.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objAcknowledgement);

                break;
            }

            case Manage_Asset_Holding: {
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    AssetHolding ah = new AssetHolding();
                    parseFromLinkedHashMapToObject(obj);
                    AssetHoldingModel ahm = (AssetHoldingModel) obj.getObject();
                    if (obj.getSqlStatementType().equals("create")) {
                        success = ah.createAssetHolding(ahm.getOrg_unit_id(), ahm.getAsset_id(), ahm.getQuantity());
                        if (success) {
                            objAcknowledgement.setMessage("Asset Holding was created.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("Asset Holding was NOT created.");
                        }
                    }
                    else if (obj.getSqlStatementType().equals("update")) {
                        success = ah.updateAssetHolding(ahm.getOrg_unit_id(), ahm.getAsset_id(), ahm.getQuantity());

                        if (success) {
                            objAcknowledgement.setMessage("Asset Holding was updated.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("Asset Holding was NOT updated");
                        }
                    }
                    objAcknowledgement.setSuccess(success ? "1" : "0");
                }
                else { // invalid password
                    objAcknowledgement.setSuccess("0");
                    objAcknowledgement.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objAcknowledgement);

                break;
            }

            case Create_Asset: {
                jsonReturn = ServerLogicCommands.createAsset(validPassword, obj, new Asset());
                break;
            }

            case Change_Password: {
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    User u = new User();
                    parseFromLinkedHashMapToObject(obj);
                    JSONChangePassword jcp = (JSONChangePassword) obj.getObject();
                    if (obj.getSqlStatementType().equals("update")) {
                        success = u.changePassword(jcp.getUsername(), jcp.getNewPassword(), jcp.getOldPassword());
                        if (success) {
                            objAcknowledgement.setMessage("Password change for '" + jcp.getUsername() + "' was successful.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("Password change for '" + jcp.getUsername() + "' was unsuccessful.");
                        }
                    }
                    objAcknowledgement.setSuccess(success ? "1" : "0");
                }
                else { // invalid password
                    objAcknowledgement.setSuccess("0");
                    objAcknowledgement.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objAcknowledgement);

                break;
            }

            case Add_Trade: {
                JSONAddTradeAcknowledgement objAddTradeAcknowledgement = new JSONAddTradeAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    String[] result;
                    TradeCurrent tc = new TradeCurrent();
                    parseFromLinkedHashMapToObject(obj);
                    TradeCurrentModel tcm = (TradeCurrentModel) obj.getObject();
                    if (obj.getSqlStatementType().equals("create")) {
                        result = tc.createTrade(tcm.getTrade_type(), tcm.getOrg_unit_id(),
                                tcm.getOrg_unit_name(), tcm.getUser_id(), tcm.getUsername(),
                                tcm.getAsset_id(), tcm.getAsset_name(), tcm.getQuantity(),
                                tcm.getPrice(), tcm.getTrade_date());
                        if (result[0].equals("3")) {
                            success = true;
                            objAddTradeAcknowledgement.setTradeId(Integer.parseInt(result[2]));
                            objAddTradeAcknowledgement.setMessage("Trade was created.");
                        }
                        else {
                            objAddTradeAcknowledgement.setErrorMessage(result[1]);
                        }
                    }
                    objAddTradeAcknowledgement.setSuccess(success ? "1" : "0");
                }
                else { // invalid password
                    objAddTradeAcknowledgement.setSuccess("0");
                    objAddTradeAcknowledgement.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objAddTradeAcknowledgement);

                break;
            }

            case Delete_Trade: {
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    TradeCurrent tc = new TradeCurrent();
                    if (obj.getSqlStatementType().equals("delete")) {
                        success = tc.deleteTrade(obj.getId());
                        if (success) {
                            objAcknowledgement.setMessage("Trade was deleted.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("Trade was not deleted.");
                        }
                    }
                    objAcknowledgement.setSuccess(success ? "1" : "0");
                }
                else { // invalid password
                    objAcknowledgement.setSuccess("0");
                    objAcknowledgement.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objAcknowledgement);

                break;
            }

            case Get_Graph_History: {
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    TradeHistory th = new TradeHistory();
                    Integer noOfTrades = th.getCountOfTradesForAsset(obj.getAssetName());
                    if (noOfTrades > 0) {
                        String jsonTradeHistory = th.getTradeHistory();
                        if (! Utilities.isNullOrEmpty(jsonTradeHistory)) {
                            objResult.setSuccess("1");
                            objResult.setResultSetType("json");
                            objResult.setResultSet(jsonTradeHistory);
                            objResult.setMessage("Trade history for asset '" + obj.getAssetName() +
                                                        "' has been retrieved." );
                        } else {
                            objResult.setSuccess("0");
                            objResult.setErrorMessage("Error when trying to retrieve trade history for '" +
                                                        obj.getAssetName() + "'.");
                        }
                    }
                    else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error occurred in retrieving trade history, check its name!");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);

                break;
            }

            case Get_Users: {
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    User u = new User();
                    String jsonAssets = u.getUsers();
                    if (! Utilities.isNullOrEmpty(jsonAssets)) {
                        objResult.setSuccess("1");
                        objResult.setResultSetType("json");
                        objResult.setResultSet(jsonAssets);
                        objResult.setMessage("Users have been retrieved.");
                    } else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error when trying to retrieve users.");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);
                break;
            }

            case Get_Assets: {
                jsonReturn = ServerLogicCommands.getAssets(validPassword, new Asset());
                break;
            }

            case Get_Asset_Holdings: {
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    AssetHolding ah = new AssetHolding();
                    String jsonAssetHoldings = ah.getAssetHoldings();
                    if (!Utilities.isNullOrEmpty(jsonAssetHoldings)) {
                        objResult.setSuccess("1");
                        objResult.setResultSetType("json");
                        objResult.setResultSet(jsonAssetHoldings);
                        objResult.setMessage("Asset holdings has been retrieved.");
                    } else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error when trying to retrieve asset holdings.");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);
                break;
            }

            case Get_Org_Units: {
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    OrgUnit ou = new OrgUnit();
                    String jsonOrgUnits = ou.getOrgUnits();
                    if (!Utilities.isNullOrEmpty(jsonOrgUnits)) {
                        objResult.setSuccess("1");
                        objResult.setResultSetType("json");
                        objResult.setResultSet(jsonOrgUnits);
                        objResult.setMessage("Organisational units has been retrieved.");
                    } else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error when trying to retrieve organisational units.");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);
                break;
            }

            case Get_Accounts: {
                jsonReturn = ServerLogicCommands.getAccounts(validPassword, new AccountType());
                break;
            }

            case Get_Current_Trades: {
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    TradeCurrent trade = new TradeCurrent();
                    String jsonCurrentTrades = trade.getTrades();
                    if (!Utilities.isNullOrEmpty(jsonCurrentTrades)) {
                        objResult.setSuccess("1");
                        objResult.setResultSetType("json");
                        objResult.setResultSet(jsonCurrentTrades);
                        objResult.setMessage("Current trades have been retrieved.");
                    } else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error when trying to retrieve current trades.");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);

                break;
            }

            case Get_Latest_Trade_Message: {
                JSONTradeMessageAcknowledgement objResult = new JSONTradeMessageAcknowledgement ();
                final String tradeType = "BUY"; // locked in at 'BUY'

                if (validPassword) {
                    TradeHistory th = new TradeHistory();
                    String latestTradeMessage = th.getLatestTradeMessage(tradeType, obj.getOrgUnitName());
                    if (!Utilities.isNullOrEmpty(latestTradeMessage)) {
                        objResult.setSuccess("1");
                        objResult.setLatestTradeMessage(latestTradeMessage);
                        objResult.setMessage("Latest trade message has been retrieved.");
                    } else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error when trying to retrieve trade message.");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);

                break;
            }

            default: {
                LOGGER.log(Level.INFO, "Server:Logic - Command: " + CmdType.name() + " unknown.");
                break;
            }
        }

        return jsonReturn;
    }

    /**
     * Used to convert an object contained with JSONAction to it actual type. Jackson during the serialisation
     * process does not have enough information to convert the object so it converts it to an intermediate
     * object of type LinkedHashMap. This method contains the the mechanism to provide this conversion.
     *
     * @param jsonAction a JSONAction object that contains an object that has to be converted.
     */
    private void parseFromLinkedHashMapToObject(JSONAction jsonAction) {
        // Jackson does not know how to de-serialise the object so this intermediate step is required. See
        //https://stackoverflow.com/questions/15430715/casting-linkedhashmap-to-complex-object for more details.

        if (jsonAction.getObjectType() != null) {
            if (jsonAction.getObjectType().equals("UserModel")) {
                UserModel pojo = objectMapper.convertValue(jsonAction.getObject(), UserModel.class);
                jsonAction.setObject(pojo);
            }
            else if (jsonAction.getObjectType().equals("OrgUnitModel")) {
                OrgUnitModel pojo = objectMapper.convertValue(jsonAction.getObject(), OrgUnitModel.class);
                jsonAction.setObject(pojo);
            }
            else if (jsonAction.getObjectType().equals("AssetHoldingModel")) {
                AssetHoldingModel pojo = objectMapper.convertValue(jsonAction.getObject(), AssetHoldingModel.class);
                jsonAction.setObject(pojo);
            }
            else if (jsonAction.getObjectType().equals("AssetModel")) {
                AssetModel pojo = objectMapper.convertValue(jsonAction.getObject(), AssetModel.class);
                jsonAction.setObject(pojo);
            }
            else if (jsonAction.getObjectType().equals("TradeCurrentModel")) {
                TradeCurrentModel pojo = objectMapper.convertValue(jsonAction.getObject(), TradeCurrentModel.class);
                jsonAction.setObject(pojo);
            }
            else if (jsonAction.getObjectType().equals("JSONLogin")) {
                JSONLogin pojo = objectMapper.convertValue(jsonAction.getObject(), JSONLogin.class);
                jsonAction.setObject(pojo);
            }
            else if (jsonAction.getObjectType().equals("JSONChangePassword")) {
                JSONChangePassword pojo = objectMapper.convertValue(jsonAction.getObject(), JSONChangePassword.class);
                jsonAction.setObject(pojo);
            }
        }
    }
}
