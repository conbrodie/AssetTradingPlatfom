package AssetTrading.server;

import AssetTrading.common.message.DataTransfer;
import AssetTrading.common.transport.JSONAction;
import AssetTrading.common.transport.JSONLogin;
import AssetTrading.common.transport.JSONLoginResult;
import AssetTrading.common.transport.JSONMessageType;
import AssetTrading.server.dal.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import AssetTrading.common.Constants;
import AssetTradingTest.common.common.transport.*;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread {
    /*
        A new ServerThread object is created for each client that connects to the Server. This thread
        creation occurs in the main Server.

        Connection steps:
        (1) On connection the client sends a MessageType 'Check, LogIn, Action' message.
        (2) Each message type is managed differently using a 'switch' statement.
        (3) The client can send 'Action' type messages, these messages are processed within the business Logic object.
            This to separate, so it can be easily maintained.
        (4) The client can terminate the process by sending 'bye', this will terminate the thread / session.
    */

    // Variables
    protected Socket clientSocket = null;
    protected String clientName = null;
    protected Logger LOGGER = null;
    private Hashtable<String, Server.ClientState> clients;
    private String message = "";
    private boolean validPassword = false;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Constructor
    public ServerThread(Logger Logger, Socket clientSocket, String clientName, Hashtable<String, Server.ClientState> clients, String message) {
        this.LOGGER = Logger;
        this.clientSocket = clientSocket;
        this.clientName = clientName;
        this.clients = clients;
        this.message = message;
    }

    @Override
    public void run() {

        Server.ClientState state = this.clients.get(clientName);

        try {
            //clientSocket.setSoTimeout(1000); // TODO ? check if needed - think not!
            System.out.println("server.Server's client.Client Thread : Waiting for the client request.");
            String clientRequest = "";

            do {
                // Will block here until client sends initial and all other messages.
                clientRequest = DataTransfer.receiveMessage(clientSocket); // waiting for a client's message request <<<<<<<
                System.out.println("Server's Client Thread : " + clientRequest + " from Server!");
                if (! clientRequest.equals("bye")) { // manages the client logging off or closing the client (by-pass all code)
                    JSONMessageType objMessageType = new JSONMessageType();
                    objMessageType = objectMapper.readValue(clientRequest, JSONMessageType.class);

                    Constants.MessageType type = Constants.MessageType.valueOf(objMessageType.getMessageType());
                    switch (type) {
                        case Check: {
                            // Check - check connection
                            DataTransfer.sendMessage(clientSocket, "Ok"); // keep the common.message flow
                            break;
                        }
                        case LogIn: {
                            // Login - check password
                            DataTransfer.sendMessage(clientSocket, "Ok"); // keep the common.message flow

                            clientRequest = DataTransfer.receiveMessage(clientSocket);
                            JSONLogin objLogin = objectMapper.readValue(clientRequest, JSONLogin.class);
                            // Check if stored password is correct for the username
                            String userPassword = objLogin.getPassword(); // hashed password from user to be tested
                            // Get credentials from database.
                            User user = new User();
                            String retrivedPassword = user.getUserSecurityCredentials(objLogin.getUsername());

                            String jsonResponse = "";
                            JSONLoginResult objResult = new JSONLoginResult();
                            if (retrivedPassword != null) { // this will occur if user does not exist in db
                                if (userPassword.equals(retrivedPassword)) { // password is ok
                                    validPassword = true;
                                    String userDetails = user.getUserDetails(objLogin.getUsername());
                                    objResult.setSuccess("1");
                                    objResult.setPassword(retrivedPassword);
                                    objResult.setJsonUserDetails(userDetails);
                                    objResult.setClientName(clientName);
                                    objResult.setMessage("User '" + objLogin.getUsername() + "' is logged in!");
                                    LOGGER.log(Level.INFO, "User '" + objLogin.getUsername() + "' has signed in successfully");
                                }
                                else { // invalid
                                    objResult.setSuccess("0");
                                    objResult.setPassword("");
                                    objResult.setClientName(clientName);
                                    objResult.setErrorMessage("Please check username and password values!");
                                }
                            }
                            else { // username not in database
                                objResult.setSuccess("0");
                                objResult.setErrorMessage("Username unknown!");
                            }
                            jsonResponse = objectMapper.writeValueAsString(new JSONLoginResult(objResult));
                            DataTransfer.sendMessage(clientSocket, jsonResponse); // send LoginResponse to client
                            break;
                        }
                        case Action: {
                            // Process message as an action message
                            ServerLogic logic = new ServerLogic(LOGGER);
                            DataTransfer.sendMessage(clientSocket, "Ok"); // keep the flow

                            clientRequest = DataTransfer.receiveMessage(clientSocket);
                            JSONAction objAction = new JSONAction();
                            objAction = objectMapper.readValue(clientRequest, JSONAction.class);
                            LOGGER.log(Level.INFO, "Action '" + objAction.getCommand() + "' has been requested.");
                            String jsonResult = logic.ProcessCommand(objAction, "", validPassword); // TODO: check ?
                            LOGGER.log(Level.INFO, "Action '" + objAction.getCommand() + "' processed for user '" + objAction.getUsername() + "'."); // TODO: username ?
                            DataTransfer.sendMessage(clientSocket, jsonResult); // send Action's outcome to client
                            break;
                        }
                        case Reconcile_Message: {
                            // TODO: get message back to client ???
                            LOGGER.log(Level.INFO, "Message: " + message);
                            break;
                        }
                        default: {
                            // TODO: Log error - no Action. Is this required?
                            LOGGER.log(Level.WARNING, "Command message" +
                                    objMessageType.getMessageType() + " not known!");
                            break;
                        }
                    }
                }
            } while (! clientRequest.equals("bye"));

            System.out.println("client.Client says '" + clientRequest + "'"); // debug statement
            LOGGER.log(Level.INFO, "client.Client says '" + clientRequest + "'"); // debug statement

            // The client has issued a 'bye' request (eg. Logout)
            Thread thread = state.getClientThread();
            System.out.println("Server Before: " + clientName + ", " + thread); // debug code
            clients.remove(clientName);
            try {
                thread.join(1500); // can adjust as necessary i.e. to complete a task...
            }  catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Thread NOT closed as expected: " + e.getMessage());
                System.out.println("Thread for '" + clientName + "' was NOT closed.");
            }
        }
        catch (SocketException e) { // forced exception - only way to remove block. 'clientRequest = DataTransfer.receiveMessage(clientSocket);'
            LOGGER.log(Level.WARNING, "SocketException for '" + clientName + "' " + e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "TCP server.Server IOException: " + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "TCP server.Server Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}