import java.io.PrintStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * thread for listening and executing client command inputs
 */
public class ClientConnection extends Thread{

    ArrayList<ClientConnection> connections;
    Socket socket;
    Scanner inputStream;
    PrintStream outputStream;

    Connection myConnection;

    String username;

    ClientConnection(String threadName, Socket socket, ArrayList<ClientConnection> connections){
        super(threadName);
        this.socket=socket;
        this.connections = connections;

    }

    /**
     * runs a thread for listening to client command inputs (and executes said commands)
     */
    @Override
    public void run() {
        try{

            /*
            end, exit, quit, stop : disconnects from the HCServer
             */
            inputStream = new Scanner(socket.getInputStream());
            outputStream = new PrintStream(socket.getOutputStream());
            String nextLine = inputStream.nextLine();
            while(!nextLine.equals("end") && !nextLine.equals("exit") && !nextLine.equals("quit") && !nextLine.equals("stop")){

                Scanner scan = new Scanner(nextLine);
                String command = scan.next();

                if(command.equals("connect")){
                    commandConnect();
                }

                if(command.equals("disconnect")){
                    commandDisconnect();
                }

                if(command.equals("authenticate")){
                    String username = "";
                    String password = "";
                    if(scan.hasNext()){
                        username = scan.next();
                    }
                    if(scan.hasNext()){
                        password = scan.next();
                    }
                    commandAuthenticate(username, password);
                }

                if(command.equals("getprofile")){
                    String username = "";
                    if(scan.hasNext()){
                        username = scan.next();
                    }
                    commandGetProfile(username);
                }

                nextLine = inputStream.nextLine();
            }

            outputStream = new PrintStream(socket.getOutputStream());
        }catch(Exception e){
            System.out.println(getName() + " disconnected");
        }
        //removes self from connections
        connections.remove(this);
    }

    /**
     * connects to SQL database
     */
    void commandConnect(){
        try{
            myConnection = DriverManager.getConnection("jdbc:mysql://classdb.it.mtu.edu:3307/huskyconnect","huskyconnect_rw","C0nn3ct!");
            System.out.println(this.getName() + " connected to database");
            outputStream.println("connected to database");
        }catch (SQLException e){
            System.out.println(this.getName() + " failed to connect to database");
            outputStream.println("failed to connect to database");
        }

    }

    /**
     * disconnects from SQL database
     */
    void commandDisconnect(){
        try{
            myConnection.close();
            System.out.println(this.getName() + " disconnected from database");
            outputStream.println("disconnected from database");
        }catch (SQLException e){
            System.out.println(this.getName() + " failed to disconnect from database");
            outputStream.println("failed to disconnect to database");
        }

    }

    /**
     * authenticates the user, and saves authenticated user in this class instance in the username var
     *
     * sends a bool to the client on weather or not the authentication was successful
     *
     * @param username user's username
     * @param password user's password
     */
    void commandAuthenticate(String username, String password){
        try{
            String sql = "select username, user_password from huskyconnect.user_profile where username = '" + username + "'";
            Statement statement = myConnection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            result.next();
            if(result.getString("user_password").equals(password)){
                this.username = username;
                outputStream.print(true);
                System.out.println(this.getName() + " logged in as " + username);
            }else{
                outputStream.print(false);
            }
        }catch (SQLException e){
            System.out.println("SQL failed to run");
            outputStream.print(false);
        }


    }

    /**
     * retrieves information on a profile
     *
     * sends information to the client
     *
     * @param username of the profile being retrieved
     */
    void commandGetProfile(String username){
        try{
            String sql = "select username, bio from huskyconnect.user_profile where username = '" + username + "'";
            Statement statement = myConnection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            result.next();
            outputStream.println(result.getString("bio"));
        }catch (SQLException e){
            System.out.println("SQL failed to run");
        }
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Scanner getInputStream() {
        return inputStream;
    }

    public void setInputStream(Scanner inputStream) {
        this.inputStream = inputStream;
    }

    public PrintStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }
}