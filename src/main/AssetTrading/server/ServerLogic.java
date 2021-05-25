package AssetTrading.server;

import AssetTrading.common.models.*;
import AssetTrading.common.transport.JSONAcknowledgement;
import AssetTrading.common.transport.JSONAction;
import AssetTrading.common.transport.JSONChangePassword;
import AssetTrading.common.transport.JSONResultset;
import AssetTrading.server.dal.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import AssetTrading.common.Constants;
import AssetTradingTest.common.common.models.*;
import AssetTradingTest.common.common.transport.*;
import AssetTradingTest.common.server.dal.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static AssetTrading.common.Utilities.isNullOrEmpty;

public class ServerLogic {

    private Logger LOGGER;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Constructor
    public ServerLogic(Logger logger ) { this.LOGGER = logger; }

    public String ProcessCommand(JSONAction obj, String username,
                                 Boolean validPassword) throws JsonProcessingException, SQLException {
        /**
         * Used to execute the required Action Command. The command must be in the enumerated ordinal 'Action'.
         * @param Action object, valid password result, username
         * @return jsonReturn, dependent on the requested command
         */

        String cmd = obj.getCommand(); // command type eg. 'Manage_User'
        Constants.Action CmdType = Constants.Action.valueOf(obj.getCommand()); // used to find the correct command/action

        String jsonReturn = ""; // return string for all methods enclosed in 'switch statement'

        switch (CmdType) {
            case Manage_User: {
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    User u = new User();
                    // need to convert to User object due to Jackson serialisation as it
                    // did not know enough about what the object was during the process.
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
                            //objAcknowledgement.setMessage("userId: " + um.getUser_id() + ", username: " + um.getUsername() + ", unitId: " + um.getOrg_unit_id() + ", accId: " + um.getAccount_type_id());
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
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

                if (validPassword) {
                    boolean success = false;
                    Asset a = new Asset();
                    parseFromLinkedHashMapToObject(obj);
                    AssetModel am = (AssetModel) obj.getObject();
                    if (obj.getSqlStatementType().equals("create")) {
                        success = a.createAsset(am.getAsset_id(), am.getAsset_name());
                        if (success) {
                            objAcknowledgement.setMessage("Asset '" + am.getAsset_name() + "' was created.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage("Asset '" + am.getAsset_name() + "' was not created.");
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
                JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

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
                            objAcknowledgement.setMessage("Trade was created.");
                        }
                        else {
                            objAcknowledgement.setErrorMessage(result[1]);
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
                        if (! isNullOrEmpty(jsonTradeHistory)) {
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
                    if (! isNullOrEmpty(jsonAssets)) {
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
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    Asset a = new Asset();
                    String jsonAssets = a.getAssets();
                    if (! isNullOrEmpty(jsonAssets)) {
                        objResult.setSuccess("1");
                        objResult.setResultSetType("json");
                        objResult.setResultSet(jsonAssets);
                        objResult.setMessage("Assets has been retrieved.");
                    } else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error when trying to retrieve assets.");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);
                break;
            }
            case Get_Asset_Holdings: {
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    AssetHolding ah = new AssetHolding();
                    String jsonAssetHoldings = ah.getAssetHoldings();
                    if (!isNullOrEmpty(jsonAssetHoldings)) {
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
                    if (!isNullOrEmpty(jsonOrgUnits)) {
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
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    AccountType at = new AccountType();
                    String jsonAccountTypes = at.getAccounts();
                    if (!isNullOrEmpty(jsonAccountTypes)) {
                        objResult.setSuccess("1");
                        objResult.setResultSetType("json");
                        objResult.setResultSet(jsonAccountTypes);
                        objResult.setMessage("Account types have been retrieved.");
                    } else {
                        objResult.setSuccess("0");
                        objResult.setErrorMessage("Error when trying to retrieve account types.");
                    }
                }
                else { // invalid password
                    objResult.setSuccess("0");
                    objResult.setErrorMessage("Password was not valid, Are you logged in!");
                }
                jsonReturn = objectMapper.writeValueAsString(objResult);
                break;
            }
            case Get_Current_Trades: {
                JSONResultset objResult = new JSONResultset();

                if (validPassword) {
                    TradeCurrent trade = new TradeCurrent();
                    String jsonCurrentTrades = trade.getTrades();
                    if (!isNullOrEmpty(jsonCurrentTrades)) {
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
            default: {
                LOGGER.log(Level.INFO, "Server:Logic - Command: " + CmdType.name() + " unknown.");
                break;
            }
        }

        return jsonReturn;
    }

    private void parseFromLinkedHashMapToObject(JSONAction o) {
        // Jackson does not know how to deserialise the object so this intermediate step is required. See
        //https://stackoverflow.com/questions/15430715/casting-linkedhashmap-to-complex-object for more details.

        if (o.getObjectType() != null) {
            if (o.getObjectType().equals("UserModel")) {
                UserModel pojo = objectMapper.convertValue(o.getObject(), UserModel.class);
                o.setObject(pojo);
            }
            else if (o.getObjectType().equals("OrgUnitModel")) {
                OrgUnitModel pojo = objectMapper.convertValue(o.getObject(), OrgUnitModel.class);
                o.setObject(pojo);
            }
            else if (o.getObjectType().equals("AssetHoldingModel")) {
                AssetHoldingModel pojo = objectMapper.convertValue(o.getObject(), AssetHoldingModel.class);
                o.setObject(pojo);
            }
            else if (o.getObjectType().equals("AssetModel")) {
                AssetModel pojo = objectMapper.convertValue(o.getObject(), AssetModel.class);
                o.setObject(pojo);
            }
            else if (o.getObjectType().equals("TradeCurrentModel")) {
                TradeCurrentModel pojo = objectMapper.convertValue(o.getObject(), TradeCurrentModel.class);
                o.setObject(pojo);
            }
            else if (o.getObjectType().equals("JSONChangePassword")) {
                JSONChangePassword pojo = objectMapper.convertValue(o.getObject(), JSONChangePassword.class);
                o.setObject(pojo);
            }
        }
    }
}
