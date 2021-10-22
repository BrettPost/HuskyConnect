import databaseconnections.ServerConnection;
import userinterface.GUI;

import java.io.IOException;

public class Main {
    static ServerConnection serverConnection;

    public static void main(String... args) {
        try{
            connectToServer();
        }catch (Exception e){
            System.out.println("failed to connect to server");
        }

        GUI.serverConnection = serverConnection;
        GUI.start();

    }

    /**
     * connects to server
     */
    static void connectToServer() throws IOException {
        serverConnection = new ServerConnection();
        serverConnection.connect("localhost",1342);

        serverConnection.commandConnect();
        }
}
