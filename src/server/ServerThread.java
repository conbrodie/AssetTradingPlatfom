package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.message.DataTransfer;
import common.transport.*;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A new ServerThread object is created for each client that connects to the Server. This thread
 * creation occurs in the main Server.
 *
 *  Connection steps:
 *  (1) The client can send 'Action' type messages, these messages are processed within the business Logic object.
 *      This to separate, so it can be easily maintained.
 *  (2) The client can terminate the process by sending 'bye', this will terminate the thread / session.
 *
 *  Note: this thread can be forcibly set-up to terminate itself by the server ie. if the server is shut down
 *  before the connection is closed by client. Not an ideal situation!
 */
public class ServerThread extends Thread {
    // Variables
    private Socket clientSocket = null;
    private String clientName = null;
    private Logger LOGGER = null;
    private Hashtable<String, Server.ClientState> clients;
    private String message;

    private final ServerLogic logic;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param Logger LOGGER variable allows the thread to log messages
     * @param clientSocket Clients socket for sending/receiving messages
     * @param clientName Name for this client - to identify it
     * @param clients Hashtable containing all client connects that are current - the server keeps this list
     * @param message Message to client
     */
    public ServerThread(Logger Logger, Socket clientSocket, String clientName, Hashtable<String,
                                                            Server.ClientState> clients, String message) {
        this.LOGGER = Logger;
        logic = new ServerLogic(LOGGER);
        this.clientSocket = clientSocket;
        this.clientName = clientName;
        this.clients = clients;
        this.message = message;
    }

    @Override
    public void run() {
        Server.ClientState state = this.clients.get(clientName); // get the state object for this thread

        try {
            System.out.println("server.Server's client.Client Thread : Waiting for the client request."); // TODO: debug code
            String clientRequest;
            do {
                // Will block here until client sends initial and all other messages.
                clientRequest = DataTransfer.receiveMessage(clientSocket); // waiting for a client's message request <<<<<<<
                // System.out.println("Server's Client Thread : " + clientRequest + " from Server!"); // TODO: debug code
                if (! clientRequest.equals("bye")) { // manages the client logging off or closing the client (by-pass all code)
                    JSONAction objAction = objectMapper.readValue(clientRequest, JSONAction.class);
                    //LOGGER.log(Level.INFO, "Action '" + objAction.getCommand() + "' has been requested."); // TODO: debug code
                    String jsonResult = logic.ProcessCommand(objAction, objAction.getUsername());
                    //LOGGER.log(Level.INFO, "Action '" + objAction.getCommand() +
                    // "' processed for user '" + objAction.getUsername() + "'."); // TODO: debug code
                    DataTransfer.sendMessage(clientSocket, jsonResult); // send Action's outcome to client
                }
            } while (! clientRequest.equals("bye"));

            System.out.println("Client says '" + clientRequest + "'"); // TODO: debug code
            LOGGER.log(Level.INFO, "Client says '" + clientRequest + "'"); // TODO:  debug statement

            // The client has issued a 'bye' request (eg. Logout) so remove client from list held by server
            // and close this thread.
            Thread thread = state.getClientThread();
            System.out.println("Server Before: " + clientName + ", " + thread); // TODO: debug code
            clients.remove(clientName);
            try {
                thread.join(1500); // 1.5 secs - can adjust as necessary i.e. to complete a task...
            }  catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Thread NOT closed as expected: " + e.getMessage());
                System.out.println("Thread for '" + clientName + "' was NOT closed."); // TODO: debug code
            }
        }
        catch (SocketException e) { // can be a forced exception - only way to remove
            // block. 'clientRequest = DataTransfer.receiveMessage(clientSocket);' see above approx line 56
            LOGGER.log(Level.WARNING, "SocketException for '" + clientName + "' " + e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "TCP Server IOException: " + e.getMessage());
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "TCP Server Exception: " + e.getMessage());
        }
    }
}