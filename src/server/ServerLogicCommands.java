package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Utilities;
import common.transport.JSONAction;
import common.transport.JSONResultset;
import common.transport.JSONTradeMessageAcknowledgement;
import server.dal.AccountTypeDb;
import server.dal.TradeHistory;

import java.util.logging.Level;

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

    public static String getLatestTradeMessage(boolean isLoggedIn, JSONAction obj) throws JsonProcessingException {
        JSONTradeMessageAcknowledgement objResult = new JSONTradeMessageAcknowledgement ();
        final String tradeType = "BUY"; // locked in at 'BUY'

        if (isLoggedIn) {
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
        return objectMapper.writeValueAsString(objResult);
    }
}
