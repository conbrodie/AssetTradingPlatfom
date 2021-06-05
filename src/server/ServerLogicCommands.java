package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Utilities;
import common.transport.JSONResultset;
import server.dal.AccountTypeDb;

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
}
