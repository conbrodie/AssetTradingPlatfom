package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.message.DataTransfer;
import common.models.TradeHistoryModel;
import common.security.Security;
import common.transport.*;
import common.models.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Used to hold all the Client's business logic. The data comes in using a Dialog type form
 * the validated data is passed to an appropriate method in this class and then the data is
 * packaged into a object. This object is then converted to a structured text message transported to the serer
 * the server does what it has to and a structured text message is passed back to the Client.
 * The client knows what to expect, so it de-serialises it into an appropriate object to be passed back to
 * the Dialog form to create appropriate message (success or failure) to the user and display other information as
 * required.
 */
public class ClientLogic {
    // Networking variables
    private String name = "Client";
    private String serverHost = "localhost";
    private int serverPort = 9000;
    private Socket clientSocket = null;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private boolean isConnected = false;
    private String username;

    /**
     * Constructor
     * @param name name of client
     * @param host host to connect to
     * @param port port
     */
    public ClientLogic(String name, String host, Integer port) {
        this.name = name;
        this.serverHost = host;
        this.serverPort = port;
    }

    /**
     * Used to connect to server. Will create a new Client Socket
     * @return success or failure
     */
    private boolean ConnectToSever() {
        if (isConnected && clientSocket != null) {
            return true;
        }
        else {
            try {
                clientSocket = new Socket(this.serverHost, this.serverPort);
                isConnected = true;
            } catch (IOException e) {
                isConnected = false;
                System.out.println("Could not connect to server!"); // TODO: debug code
            }
        }
        return isConnected;
    }

    /**
     * Used to close server connection and send 'bye' message to server
     */
    private void CloseServerConnection() {
        try {
            if (clientSocket != null) {
                DataTransfer.sendMessage(clientSocket, "bye");
                clientSocket.close();
                clientSocket = null;
                isConnected = false;
            }
        } catch (IOException e) {
            clientSocket = null;
            isConnected = false;
            System.out.println("Could not close connection to server!"); // TODO: debug code
        }
    }

    /**
     * Used to gracefully close server connection, Will send a 'bye' to server
     */
    public void GetOut() {
        CloseServerConnection();
    }

    /**
     * Used to set the private variable 'username' so the user can be logged, this is passed in each Action message type.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Used to validate user supplied username and password.
     * 1) Hash text password
     * 2) send to server for validation
     * 3) Server will check the username and hashed password
     * 4) Receive LoginResult message from server. This will contain success '1' or fail '0' if
     *    the passed hashed password was not correct for the username.
     *
     * @param loginUser an object containing username and free text password entered by user
     * @return json formatted string containing success or failure and if successful user's extended details
     */
    public String ValidatePassword(JSONLogin loginUser) {
        String jsonLoginResult = "";
        System.out.println("Before Connection cmd: " + isConnected);
        boolean connectionOpen = ConnectToSever(); // open connection to Server (only here!!)

        if (connectionOpen) {
            try {
                String hashedPwd = Security.GeneratePasswordHash(loginUser.getPassword()); // hash password
                loginUser.setPassword(hashedPwd); // update freetext value with hashed value

                JSONAction obj = new JSONAction();
                obj.setCommand("LogIn"); // must be sent
                obj.setSqlStatementType("select");
                obj.setObjectType("JSONLogin");
                obj.setObject(loginUser);
                obj.setUsername("logging in");
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonLoginResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // do nothing...
            }
        }
        return jsonLoginResult;
    }

    /**
     *  Used to get the latest trade processed message from the server
     * @param orgUnitName name of the organisation unit
     * @return the latest processed trade message
     */
    public String getLatestTradeMessage(String orgUnitName) {
        String jsonTradeMessageAcknowledgement = null;
        String latestMessage = "Message not available - check again latter.";

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Latest_Trade_Message"); // must be sent
                obj.setOrgUnitName(orgUnitName);
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonTradeMessageAcknowledgement = DataTransfer.receiveMessage(clientSocket); // block - waiting for server

                // Unbundle it here as its only a message
                JSONTradeMessageAcknowledgement objResultset = new JSONTradeMessageAcknowledgement ();
                objResultset = objectMapper.readValue(jsonTradeMessageAcknowledgement, JSONTradeMessageAcknowledgement .class);
                if (objResultset.getSuccess().equals("1")) {
                    latestMessage = objResultset.getLatestTradeMessage();
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return latestMessage;
    }

    /**
     * Used to change the user's password
     * @param changePassword New hashed password to be stored for the user
     * @return Acknowledgement string that contains information and status of the request
     */
    public String changePassword(JSONChangePassword changePassword) {
        String jsonAcknowledgementResult = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Change_Password"); // must be sent
                obj.setSqlStatementType("update");
                obj.setObjectType("JSONChangePassword");
                obj.setObject(changePassword);
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonAcknowledgementResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return jsonAcknowledgementResult;
    }

    /**
     * Used to add a trade
     * @param trade Trade to be added
     * @return Acknowledgement string that contains information and status of the request
     */
    public String addTrade(TradeCurrentModel trade) {
        String jsonAcknowledgementResult = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Add_Trade"); // must be sent
                obj.setSqlStatementType("create"); // insert!
                obj.setObjectType("TradeCurrentModel");
                obj.setObject(trade);
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonAcknowledgementResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return jsonAcknowledgementResult;
    }

    /**
     * Used to delete a trade
     * @param id Trade id to be deleted
     * @return Acknowledgement string that contains information and status of the request
     */
    public String deleteTrade(int id) {
        // Delete trade using its Id
        String jsonAcknowledgementResult = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Delete_Trade"); // must be sent
                obj.setSqlStatementType("delete"); // delete!
                obj.setId(id);
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);
                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonAcknowledgementResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return jsonAcknowledgementResult;
    }

    /**
     * Get all current trades
     * @return current trades
     * @throws IOException
     */
    public ArrayList<TradeCurrentModel> getAllCurrentTrades() throws IOException {
        // Get all current trades
        ArrayList<TradeCurrentModel> currentTrades = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Current_Trades"); // must be sent
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                String jsonResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server

                JSONResultset objResultset = new JSONResultset();
                objResultset = objectMapper.readValue(jsonResult, JSONResultset.class);
                if (objResultset.getSuccess().equals("1")) {
                    TradeCurrentModel[] lst = objectMapper.readValue(objResultset.getResultSet(), TradeCurrentModel[].class);
                    currentTrades = new ArrayList(Arrays.asList(lst));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return currentTrades;
    }

    /**
     * Get all assets
     * @return all assets
     * @throws IOException
     */
    public ArrayList<AssetModel> getAllAssets() throws IOException {
        // Get all assets
        ArrayList<AssetModel> assets = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Assets"); // must be sent
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);
                DataTransfer.sendMessage(clientSocket, jsonAction);

                String jsonResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
                JSONResultset objResultset = new JSONResultset();
                objResultset = objectMapper.readValue(jsonResult, JSONResultset.class);
                if (objResultset.getSuccess().equals("1")) {
                    AssetModel[] lst = objectMapper.readValue(objResultset.getResultSet(), AssetModel[].class);
                    assets = new ArrayList(Arrays.asList(lst));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return assets;
    }

    /**
     * Get all users
     * @return all users
     * @throws IOException
     */
    public ArrayList<UserModel> getAllUsers() throws IOException {
        // Get all users
        ArrayList<UserModel> users = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Users"); // must be sent
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);
                DataTransfer.sendMessage(clientSocket, jsonAction);

                String jsonResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
                JSONResultset objResultset = new JSONResultset();
                objResultset = objectMapper.readValue(jsonResult, JSONResultset.class);
                if (objResultset.getSuccess().equals("1")) {
                    UserModel[] lst = objectMapper.readValue(objResultset.getResultSet(), UserModel[].class);
                    users = new ArrayList(Arrays.asList(lst));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return users;
    }

    /**
     * Get all organisation units
     * @return all organisation units
     * @throws IOException
     */
    public ArrayList<OrgUnitModel> getAllOrgUnits() throws IOException {
        // Get all org units
        ArrayList<OrgUnitModel> orgUnits = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Org_Units"); // must be sent
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);
                DataTransfer.sendMessage(clientSocket, jsonAction);

                String jsonResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
                JSONResultset objResultset = new JSONResultset();
                objResultset = objectMapper.readValue(jsonResult, JSONResultset.class);
                if (objResultset.getSuccess().equals("1")) {
                    OrgUnitModel[] lst = objectMapper.readValue(objResultset.getResultSet(), OrgUnitModel[].class);
                    orgUnits = new ArrayList(Arrays.asList(lst));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return orgUnits;
    }

    /**
     * Get all account types
     * @return all account types
     * @throws IOException
     */
    public ArrayList<AccountTypeModel> getAllAccountTypes() throws IOException {
        // Get all account types
        ArrayList<AccountTypeModel> accountTypes = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Accounts"); // must be sent
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);
                DataTransfer.sendMessage(clientSocket, jsonAction);

                String jsonResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
                JSONResultset objResultset = new JSONResultset();
                objResultset = objectMapper.readValue(jsonResult, JSONResultset.class);
                if (objResultset.getSuccess().equals("1")) {
                    AccountTypeModel[] lst = objectMapper.readValue(objResultset.getResultSet(), AccountTypeModel[].class);
                    accountTypes = new ArrayList(Arrays.asList(lst));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return accountTypes;
    }

    /**
     * Get all asset holdings
     * @return all asset holdings
     * @throws IOException
     */
    public ArrayList<AssetHoldingModel> getAllAssetHoldings() throws IOException {
        // Get all asset holdings
        ArrayList<AssetHoldingModel> assetHoldings = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Asset_Holdings"); // must be sent
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);
                DataTransfer.sendMessage(clientSocket, jsonAction);

                String jsonResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
                JSONResultset objResultset = new JSONResultset();
                objResultset = objectMapper.readValue(jsonResult, JSONResultset.class);
                if (objResultset.getSuccess().equals("1")) {
                    AssetHoldingModel[] lst = objectMapper.readValue(objResultset.getResultSet(), AssetHoldingModel[].class);
                    assetHoldings = new ArrayList(Arrays.asList(lst));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return assetHoldings;
    }

    /**
     * Used to manage user information - create and update
     * @param userId users id
     * @param username username
     * @param hashedPassword hashed password
     * @param accountTypeId account_id
     * @param orgUnitId org_unit_id
     * @param sqlStatementType create, update or select
     * @return Acknowledgement string that contains information and status of the request
     */
    public String processUser(int userId, String username, String hashedPassword, int accountTypeId,
                              int orgUnitId, String sqlStatementType) {
        String jsonAcknowledgementResult = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Manage_User"); // must be sent
                obj.setSqlStatementType(sqlStatementType);
                obj.setObjectType("UserModel");
                obj.setObject(new UserModel(userId, username, hashedPassword, accountTypeId, orgUnitId));
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonAcknowledgementResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return jsonAcknowledgementResult;
    }

    /**
     * Used to add an asset
     * @param asset_id asset id
     * @param asset_name asset name
     * @return Acknowledgement string that contains information and status of the request
     */
    public String addAsset(int asset_id, String asset_name) {

        String jsonAcknowledgementResult = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Create_Asset"); // must be sent
                obj.setSqlStatementType("create");
                obj.setObjectType("AssetModel");
                obj.setObject(new AssetModel(asset_id, asset_name));
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonAcknowledgementResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return jsonAcknowledgementResult;
    }

    /**
     * Used to manage the asset holding of an organisation
     * @param orgUnitId org unit id
     * @param assetId asset id
     * @param quantity quantity of assets held by organisation
     * @param sqlStatementType create, update or select
     * @return Acknowledgement string that contains information and status of the request
     */
    public String processAssetHolding(int orgUnitId, int assetId, int quantity, String sqlStatementType) {
        String jsonAcknowledgementResult = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Manage_Asset_Holding"); // must be sent
                obj.setSqlStatementType(sqlStatementType);
                obj.setObjectType("AssetHoldingModel");
                obj.setObject(new AssetHoldingModel(orgUnitId, assetId, quantity));
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonAcknowledgementResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return jsonAcknowledgementResult;
    }

    /**
     * Used to manage the Organisation unit
     * @param orgUnitId org unit id
     * @param orgUnitName organisation name
     * @param quantity quantity of assets held by organisation
     * @param sqlStatementType create, update or select
     * @return Acknowledgement string that contains information and status of the request
     */
    public String processOrgUnit(int orgUnitId, String orgUnitName, int quantity, String sqlStatementType) {
        String jsonAcknowledgementResult = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Manage_Org_Unit"); // must be sent
                obj.setSqlStatementType(sqlStatementType);
                obj.setObjectType("OrgUnitModel");
                obj.setObject(new OrgUnitModel(orgUnitId, orgUnitName, quantity));
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);

                DataTransfer.sendMessage(clientSocket, jsonAction);
                jsonAcknowledgementResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return jsonAcknowledgementResult;
    }

    /**
     * Get the trade history for an asset
     * @param assetName asset name
     * @return an array of trade history for an asset
     */
    public ArrayList<TradeHistoryModel> getTradeHistory(String assetName) {

        // Get all current trades
        ArrayList<TradeHistoryModel> tradeHistory = null;

        if (isConnected) {
            try {
                JSONAction obj = new JSONAction();
                obj.setCommand("Get_Graph_History"); // must be sent
                obj.setAssetName(assetName);
                obj.setUsername(username);
                String jsonAction = objectMapper.writeValueAsString(obj);
                DataTransfer.sendMessage(clientSocket, jsonAction);

                String jsonResult = DataTransfer.receiveMessage(clientSocket); // block - waiting for server
                JSONResultset objResultset = new JSONResultset();
                objResultset = objectMapper.readValue(jsonResult, JSONResultset.class);
                if (objResultset.getSuccess().equals("1")) {
                    TradeHistoryModel[] lst = objectMapper.readValue(objResultset.getResultSet(), TradeHistoryModel[].class);
                    tradeHistory = new ArrayList(Arrays.asList(lst));
                }
            } catch (IOException e) {
                e.printStackTrace(); // TODO:
            } finally {
                // do nothing...
            }
        }
        return tradeHistory;
    }
}