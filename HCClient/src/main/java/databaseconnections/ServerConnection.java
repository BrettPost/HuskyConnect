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

    private Scanner inputStream;
    private PrintStream outputStream;

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
     * ONLY CALLED ONCE
     *
     * @throws IOException when the connection to the database is refused
     */
    public void commandConnect() throws IOException {
        //opens the input and output streams
        inputStream = new Scanner(socket.getInputStream());
        outputStream = new PrintStream(socket.getOutputStream());

        //sends the command to the server
        outputStream.println("connect");
        String nextLine = inputStream.nextLine();
        if(nextLine.equals("failed to connect to database")){
            throw new IOException("failed to connect to database");
        }else{
            System.out.println(nextLine);
        }
    }

    /**
     * LOGIN COMMAND
     * Talks with the server to ensure the username and password are a valid pair for
     * an existing user
     *
     * Program WILL freeze or fail if server does not send back any input or invalid input
     *
     * @param username username to login to
     * @param password password for [username]'s account
     * @return true if the login is successful, false otherwise
     */
    public boolean commandLogin(String username, String password) {
        //send authenticate command to server
        try{
            outputStream.println("authenticate " + username + " " + password);
            return inputStream.nextBoolean(); //next thing server sends should be boolean
        }catch (Exception e){
            return false;
        }
    }


    /**
     * GET PROFILE COMMAND
     * Talks with the server to get the profile for the person with the given username.
     * Current implementation only retrieves the bio of the profile.
     *
     * Program WILL freeze or fail if server does not send back any input or invalid input
     *
     * @param username username to get profile of
     * @return the bio of the person with the given username, null if invalid username.
     */
    public String commandGetProfile(String username) {
        //send getprofile command to the server
        try{
            outputStream.println("getprofile " + username);

            //ensure username was valid
            if(inputStream.nextBoolean()) {
                return inputStream.nextLine(); //server should send bio next (return bio)
            }
            else {
                return null;
            }
        }catch (Exception e){
            return "N/A";
        }


    }




}
