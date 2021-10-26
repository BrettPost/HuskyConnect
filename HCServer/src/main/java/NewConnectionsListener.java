import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * thread for listening for new client connection to the server
 */
public class NewConnectionsListener extends Thread {
    ArrayList<ClientConnection> connections;
    ServerSocket serverSocket;

    public NewConnectionsListener(String threadName, ServerSocket serverSocket, ArrayList<ClientConnection> connections) {
        super(threadName);
        this.serverSocket = serverSocket;
        this.connections = connections;

    }

    /**
     * runs a thread for listening for new client connections to the server
     */
    @Override
    public void run() {

        try {
            //adds a new client connection when a client connects to the server socket
            int count = 0;
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                count++;
                ClientConnection cc = new ClientConnection("Connection #"+ count,socket, connections);
                connections.add(cc);
                cc.start();
                System.out.println("Connection #"+count+" connected");
            }
        } catch (IOException e) {
            System.out.println("Server Socket Closed");
        }

    }

}
