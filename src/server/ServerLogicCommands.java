package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Utilities;
import common.models.*;
import common.transport.*;
import server.dal.AccountTypeDb;
import server.dal.Asset;
import server.dal.AssetDb;
import server.dal.OrgUnit;

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

    public static String getLatestTradeMessage(boolean isLoggedIn, AssetDb db) throws JsonProcessingException {
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

    public static String getOrgUnits(boolean isLoggedIn, AssetDb db) throws JsonProcessingException {
        JSONResultset objResult = new JSONResultset();

        if (isLoggedIn) {
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
