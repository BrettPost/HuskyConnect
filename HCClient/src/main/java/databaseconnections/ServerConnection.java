package databaseconnections;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Is a connection to the HCServer and is responsible for sending commands over to the HCServer
 */
public class ServerConnection {

    Socket socket;

    /**
     * connects to the HC server
     * @param ip ip of the server
     * @param port port of the server
     * @throws IOException when a connection to the server is refused
     */
    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip,port);
    }

    /**
     * disconnects from the HC server
     * @throws IOException when the disconnect attempt fails
     */
    public void disconnect() throws IOException {
        socket.close();
    }

    /**
     * connects the server to the SQL database
     * @throws IOException when the connection to the database is refused
     */
    public void commandConnect() throws IOException {
        Scanner inputStream = new Scanner(socket.getInputStream());
        PrintStream outputStream = new PrintStream(socket.getOutputStream());

        //sends the command to the server
        outputStream.println("connect");
        String nextLine = inputStream.nextLine();
        if(nextLine.equals("failed to connect to database")){
            throw new IOException("failed to connect to database");
        }else{
            System.out.println(nextLine);
        }
        inputStream.close();
    }

}
