package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Utilities;
import common.models.*;
import common.transport.*;
import server.dal.*;

public class ServerLogicCommands {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String getAccounts(boolean isLoggedIn, AccountTypeDb db) throws JsonProcessingException {
        JSONResultset objResult = new JSONResultset();

        if (isLoggedIn) {
            String jsonAccountTypes = db.getAccounts();
            if (!Utilities.isNullOrEmpty(jsonAccountTypes)) {
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
        return objectMapper.writeValueAsString(objResult);
    }

    public static String createAsset(boolean isLoggedIn, JSONAction obj, AssetDb db) throws JsonProcessingException {

        JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

        if (isLoggedIn) {
            boolean success = false;
            parseFromLinkedHashMapToObject(obj);
            AssetModel am = (AssetModel) obj.getObject();
            if (obj.getSqlStatementType().equals("create")) {
                success = db.createAsset(am.getAsset_id(), am.getAsset_name());
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
        return objectMapper.writeValueAsString(objAcknowledgement);
    }

    public static String getAssets(boolean isLoggedIn, AssetDb db) throws JsonProcessingException {
        JSONResultset objResult = new JSONResultset();

        if (isLoggedIn) {
            String jsonAssets = db.getAssets();
            if (! Utilities.isNullOrEmpty(jsonAssets)) {
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
        return objectMapper.writeValueAsString(objResult);
    }

    public static String manageAssetHolding(boolean isLoggedIn, JSONAction obj, AssetHoldingDb db) throws JsonProcessingException {
        JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

        if (isLoggedIn) {
            boolean success = false;
            parseFromLinkedHashMapToObject(obj);
            AssetHoldingModel ahm = (AssetHoldingModel) obj.getObject();
            if (obj.getSqlStatementType().equals("create")) {
                success = db.createAssetHolding(ahm.getOrg_unit_id(), ahm.getAsset_id(), ahm.getQuantity());
                if (success) {
                    objAcknowledgement.setMessage("Asset Holding was created.");
                }
                else {
                    objAcknowledgement.setErrorMessage("Asset Holding was NOT created.");
                }
            }
            else if (obj.getSqlStatementType().equals("update")) {
                success = db.updateAssetHolding(ahm.getOrg_unit_id(), ahm.getAsset_id(), ahm.getQuantity());

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
        return objectMapper.writeValueAsString(objAcknowledgement);
    }

    public static String getAssetHoldings(boolean isLoggedIn, AssetHoldingDb db) throws JsonProcessingException {
        JSONResultset objResult = new JSONResultset();

        if (isLoggedIn) {
            String jsonAssetHoldings = db.getAssetHoldings();
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
        return objectMapper.writeValueAsString(objResult);
    }

    public static String getOrgUnits(boolean isLoggedIn, OrgUnitDb db) throws JsonProcessingException {
        JSONResultset objResult = new JSONResultset();

        if (isLoggedIn) {
            String jsonOrgUnits = db.getOrgUnits();
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
        return objectMapper.writeValueAsString(objResult);
    }

    public static String manageOrgUnit(boolean isLoggedIn, JSONAction obj, OrgUnitDb db) throws JsonProcessingException {
        JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

        if (isLoggedIn) {
            boolean success = false;
            parseFromLinkedHashMapToObject(obj);
            OrgUnitModel oum = (OrgUnitModel) obj.getObject();
            if (obj.getSqlStatementType().equals("create")) {
                success = db.createOrgUnit(oum.getOrg_unit_name(), oum.getCredits());
                if (success) {
                    objAcknowledgement.setMessage("Organisation '" + oum.getOrg_unit_name() + "' was created.");
                }
                else {
                    objAcknowledgement.setErrorMessage("Organisation '" + oum.getOrg_unit_name() + "' was not created.");
                }
            }
            else if (obj.getSqlStatementType().equals("update")) {
                success = db.updateOrgUnit(oum.getOrg_unit_id(), oum.getOrg_unit_name(), oum.getCredits());

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
        return objectMapper.writeValueAsString(objAcknowledgement);
    }

    public static String addTrade(boolean isLoggedIn, JSONAction obj, TradeCurrentDb db) throws JsonProcessingException {
        JSONAddTradeAcknowledgement objAddTradeAcknowledgement = new JSONAddTradeAcknowledgement();

        if (isLoggedIn) {
            boolean success = false;
            String[] result;
            parseFromLinkedHashMapToObject(obj);
            TradeCurrentModel tcm = (TradeCurrentModel) obj.getObject();
            if (obj.getSqlStatementType().equals("create")) {
                result = db.createTrade(tcm.getTrade_type(), tcm.getOrg_unit_id(),
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
        return objectMapper.writeValueAsString(objAddTradeAcknowledgement);
    }

    public static String deleteTrade(boolean isLoggedIn, JSONAction obj, TradeCurrentDb db) throws JsonProcessingException {
        JSONAcknowledgement objAcknowledgement = new JSONAcknowledgement();

        if (isLoggedIn) {
            boolean success = false;
            if (obj.getSqlStatementType().equals("delete")) {
                success = db.deleteTrade(obj.getId());
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
        return objectMapper.writeValueAsString(objAcknowledgement);
    }

    public static String getCurrentTrades(boolean isLoggedIn, TradeCurrentDb db) throws JsonProcessingException {
        JSONResultset objResult = new JSONResultset();

        if (isLoggedIn) {
            String jsonCurrentTrades = db.getTrades();
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
        return objectMapper.writeValueAsString(objResult);
    }

    public static String getGraphHistory(boolean isLoggedIn, JSONAction obj, TradeHistoryDb db) throws JsonProcessingException {
        JSONResultset objResult = new JSONResultset();

        if (isLoggedIn) {
            Integer noOfTrades = db.getCountOfTradesForAsset(obj.getAssetName());
            if (noOfTrades > 0) {
                String jsonTradeHistory = db.getTradeHistory();
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
        return objectMapper.writeValueAsString(objResult);
    }

    public static String getLatestTradeMessage(boolean isLoggedIn, JSONAction obj, TradeHistoryDb db) throws JsonProcessingException {
        JSONTradeMessageAcknowledgement objResult = new JSONTradeMessageAcknowledgement ();
        final String tradeType = "BUY"; // locked in at 'BUY'

        if (isLoggedIn) {
            String latestTradeMessage = db.getLatestTradeMessage(tradeType, obj.getOrgUnitName());
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
        return objectMapper.writeValueAsString(objResult);
    }

    /**
     * Used to convert an object contained with JSONAction to it actual type. Jackson during the serialisation
     * process does not have enough information to convert the object so it converts it to an intermediate
     * object of type LinkedHashMap. This method contains the the mechanism to provide this conversion.
     *
     * @param jsonAction a JSONAction object that contains an object that has to be converted.
     */
    private static void parseFromLinkedHashMapToObject(JSONAction jsonAction) {
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