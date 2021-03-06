package server;

import common.Utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 *     This instantiation of this class provides a Server. This server runs in its own thread and is capable
 *     of responding to one or more clients. The Server can be terminated by sending an 'exit' message by the console.
 *
 *     A new thread is created for each connection.
 *
 *     There is a 'Logic class' associated with this ServerThread class and is used to manage the Action messages
 *     from the Client. More detail is provided in the comments in the ServerThread class header.
 *
 *     The server group of classes provides all access to the database and provides security for the Client. An
 *     encryption manager is used on the Client to encrypt the plaintext password entered by the client and this encrypted
 *     password is transported to this server and stored in the database. Basic one time check only!
 *
 *     Because the server runs without a visible interface it has a logging capability via the logger. This allows the
 *     recording of information so it can be checked in-case of error or to see if the server is performing as expected.
 *
 *     Transport of message between the server and client are in the form of json formatted strings. These strings are
 *     de-serialised, acted on and the appropriate return json message is sent back to the client where it
 *     is de-serialised.
 *
 *     The server keeps a Hashtable of all client connections. It contains access to the created thread to
 *     service the client, the name given to the Client and its socket.
 *
 *     This information is used to assist in managing the client thread in particular they can be removed if the
 *     server is closed by a the administrator.
 *
 *     The server also has an ScheduledExecutorService that is used to periodically call a procedure in the database to
 *     reconcile the current trades. The initial delay and period can be adjusted via property settings or it can
 *     also be disabled using a property setting.
 */

public class Server implements Runnable {
    private boolean enableService = true;
    private int initialDelay = 15;
    private int period = 15;

    private int serverPort = 9000; // default value
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private Thread runningThread = null;

    // Setup logger
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private ScheduledExecutorService executorService = null;
    private Socket clientSocket = null;

    private Hashtable<String, ClientState> clients = new Hashtable<>();
    private static int count = 1; // to append to each client as connected, used for identification
    private String message = "";

    // Constructor
    public Server(int port, int initialDelay, int period, boolean enableService) {
        this.serverPort = port;
        this.initialDelay = initialDelay;
        this.period = period;
        this.enableService = enableService;
    }

    @Override
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();

        // Start reconcile operation ?
        Reconcile reconcile = new Reconcile(LOGGER);
        executorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture scheduledFuture =
                        executorService.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // TODO: Used to perform the reconciliation of the current trades. May be via a procedure?
                                    String message = (String) reconcile.call();
                                } catch (Exception e) {
                                    LOGGER.log(Level.WARNING, "Reconcile scheduler service failed to start - " + e.getMessage());
                                }
                            }
                        }, initialDelay, period, TimeUnit.SECONDS);
        if (! enableService) {
            executorService.shutdown();
            LOGGER.log(Level.INFO, "Reconcile scheduler service is disabled by property setting!");
        }
        else {
            LOGGER.log(Level.INFO, "Reconcile scheduler service will start in '" + initialDelay + "' seconds");
        }
        while(! isStopped()) {
            try {
                LOGGER.log(Level.INFO, "Ready to accept clients!");
                clientSocket = this.serverSocket.accept(); // will block, waiting for a client to connect. <<<<<<<<<<

                // Create new thread for the newly connected client, then return to be able to accept another client
                String clientThreadName = "Client" + count++; // increment for each connection processed
                Thread thread = new ServerThread(LOGGER, clientSocket, clientThreadName, clients, message); // create it
                ClientState state = new ClientState(thread, clientSocket); // create a ClientState object
                thread.setName(clientThreadName); // name it
                thread.start(); // start it
                clients.put(thread.getName(), state); // add thread details to container so they can be latter destroyed
                // Debug code TODO: remove
                System.out.println("Server's client values: " + thread.getName() + " | " +
                        clients.get(thread.getName()).getClientThread() + " | " +
                        clients.get(thread.getName()).getClientSocket());

                LOGGER.log(Level.INFO, clientThreadName + " has connected.");
            } catch (Exception e) { // IOException e
                if (isStopped()) {
                    System.out.println("Server was stopped");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
        }
        System.out.println("Server stopped");
    }

    // Create server socket
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

    // Check status of server by reading private variable
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    // Set private variable to start the process to stop the server
    public synchronized void stop() throws InterruptedException, IOException {
        // System.out.println("Reconcile scheduler requested to shutdown."); // TODO: debug code - remove
        if (enableService) { LOGGER.log(Level.INFO, "Reconcile scheduler requested to shutdown."); }
        if(! executorService.isShutdown()) { // check - may been commanded to not start
            executorService.shutdown(); // shutdown Reconcile operation
            if (executorService.isShutdown()) {
                System.out.println("Reconcile scheduler service has shutdown.");
                LOGGER.log(Level.INFO, "Reconcile scheduler service has shutdown.");
            }
        }
        System.out.println("Client threads: " + clients); // TODO: debug code
        LOGGER.log(Level.INFO, "Client threads: " + clients);

        closeClientConnections(); // close server client threads

        // Test code to check what threads are currently running in the JVM, have the clients been successfully closed
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread t : threads) {
            String name = t.getName();
            Thread.State state = t.getState();
            int priority = t.getPriority();
            String type = t.isDaemon() ? "Daemon" : "Normal";
            System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
        }
        //// end of debug code block ////
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server.Server", e);
        }
    }

    private void closeClientConnections() throws IOException, InterruptedException {
        // Shutdown (forcibly) all server created threads by shutting their socket to break the blocked client request

        Set<String> keys = clients.keySet();
        for ( String key : keys) {
            ClientState state = clients.get(key);
            Thread thread = state.getClientThread();

            System.out.println("Server Close Before Shutdown command applied - Id: " +
                    thread.getId() + " , State: " + thread.getState()); // TODO: debug code
            state.getClientSocket().close(); // close socket
            thread.interrupt();
            thread.join(1500); // terminate the thread
            System.out.println("Server Close After Shutdown command applied - Id: " +
                    thread.getId() + " , State: " + thread.getState()); // TODO: debug code - should not get here
        }
    }

    /**
     * Class used to hold each of the threads representing a client connection and the client socket value
     */
    public class ClientState {
        private Thread clientThread;
        private Socket clientSocket;

        public ClientState(Thread clientThread, Socket clientSocket) {
            this.clientThread = clientThread;
            this.clientSocket = clientSocket;
        }

        public Thread getClientThread() {
            return clientThread;
        }

        public void setClientThread(Thread clientThread) {
            this.clientThread = clientThread;
        }

        public Socket getClientSocket() {
            return clientSocket;
        }

        public void setClientSocket(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    // Main method...
    public static void main(String[] args) throws IOException, InterruptedException {

        // Setup logger
        Handler fileHandler = new FileHandler("./server.log", true);
        Formatter simpleFormatter = new Utilities.CustomFormatter();
        LOGGER.addHandler(fileHandler);
        fileHandler.setFormatter(simpleFormatter);
        fileHandler.setLevel(Level.ALL);

        // Get port for server to listen on from property file 'network.prop'
        int port = Integer.parseInt(Utilities.getProperty(Server.class,"network.prop", "port"));
        int initialDelay = Integer.parseInt(Utilities.getProperty(Server.class,"server.prop", "delay"));
        int period = Integer.parseInt(Utilities.getProperty(Server.class,"server.prop", "period"));
        boolean startService = Boolean.parseBoolean(Utilities.getProperty(Server.class,"server.prop", "useScheduler"));

        Server server = new Server(port, initialDelay, period, startService); // create the server object

        Boolean ok = true; // use this variable to manage checks before starting server...
        if (ok) {
            new Thread(server).start(); // start the TCP server.Server...

            // Test code here - convenient place ...

            System.out.println("Server started");
            LOGGER.log(Level.INFO, "Server started, listening for client on port: " + port);

            try {
                System.out.println("Type 'exit' to stop server ");
                // Allows the user to type in 'exit' to stop the server.
                Scanner sc = new Scanner(System.in);
                while (true) {
                    String s1 = sc.next();  // will block here waiting for user's keyboard entry. <<<<<<<<<<
                    if (s1.equals("exit")) {
                        break;
                    } // Note: server runs in a separate thread.
                }
                sc.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Server Main Exception: " + e.getMessage());
                // e.printStackTrace(); // TODO: debug code - remove
            }
            System.out.println("Stopping Server");
            LOGGER.log(Level.INFO, "Stopping Server");
            server.stop(); // stop the server
        }
        else {
            System.out.println("Something could not be found or error in creating it!");
            LOGGER.log(Level.INFO, "Something could not be found or error in creating it!");
        }
    }
}