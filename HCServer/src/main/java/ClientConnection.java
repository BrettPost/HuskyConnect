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

    ClientConnection(String threadName, Socket socket, ArrayList<ClientConnection> connections){
        super(threadName);
        this.socket=socket;
        this.connections = connections;

    }

    /**
     * runs a thread for listening to client command inputs
     */
    @Override
    public void run() {
        try{

            /*
            Lists Of Commands:
            end, exit, quit, stop : disconnects from the HCServer
            connect : connects to the sql database
            disconnect : connects to the sql database
             */
            inputStream = new Scanner(socket.getInputStream());
            outputStream = new PrintStream(socket.getOutputStream());
            String nextLine = inputStream.nextLine();
            while(!nextLine.equals("end") && !nextLine.equals("exit") && !nextLine.equals("quit") && !nextLine.equals("stop")){

                if(nextLine.equals("connect")){
                    try{
                        commandConnect();
                        System.out.println(this.getName() + " connected to database");
                        outputStream.println("connected to database");
                    }catch (SQLException e){
                        System.out.println(this.getName() + " failed to connect to database");
                        outputStream.println("failed to connect to database");
                    }
                }else{
                    outputStream.println("invalid command");
                }

                if(nextLine.equals("disconnect")){
                    try{
                        commandDisconnect();
                        outputStream.println("disconnected from database");
                    }catch (SQLException e){
                        System.out.println(this.getName() + " failed to connect to database");
                        outputStream.println("failed to disconnect to database");
                    }
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
     * @throws SQLException when failed to connect to database
     */
    void commandConnect() throws SQLException {
        myConnection = DriverManager.getConnection("jdbc:mysql://classdb.it.mtu.edu:3307/huskyconnect","huskyconnect_rw","C0nn3ct!");
    }

    /**
     * disconnects from SQL database
     * @throws SQLException when failed to disconnect from database
     */
    void commandDisconnect() throws SQLException {
        myConnection.close();
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