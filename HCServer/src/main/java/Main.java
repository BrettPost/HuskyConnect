import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<ClientConnection> connections;
    static ServerSocket serverSocket;

    public static void main(String[] args){
        connections = new ArrayList<>();

        //starts a thread to accept connections
        try{
            serverSocket = new ServerSocket(1342);
        }catch (Exception e){
            System.out.println("failed to connect to server");
            e.printStackTrace();
        }

        NewConnectionsListener newConnectionsListener = new NewConnectionsListener("Accept Connections Thread",serverSocket, connections);
        newConnectionsListener.start();

        /*
        Server-ONLY Commands:
        end, exit, quit, stop : terminates the program
        count : number of active connections
         */

        //listens for server ran commands
        Scanner serverCommandsIn = new Scanner(System.in);
        String nextLine = serverCommandsIn.nextLine();
        while(!nextLine.equals("end") && !nextLine.equals("exit") && !nextLine.equals("quit") && !nextLine.equals("stop")){

            if(nextLine.equals("count")){
                System.out.println(connections.size());
            }

            nextLine = serverCommandsIn.next();
        }

        try {
            terminate();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * attempts to safely terminate all threads of the program
     *
     * suppressWarnings for casting clone back to it's datatype
     *
     * @throws Exception when a socket fails to close
     */
    @SuppressWarnings("unchecked")
    static void terminate() throws Exception {
        //Terminate threads
        ArrayList<ClientConnection> temp = (ArrayList<ClientConnection>) connections.clone();
        for (ClientConnection clientConnection: temp) {
            clientConnection.socket.close();//will stop ClientConnection thread
        }
        serverSocket.close(); //will stop the NewConnectionsListener thread

    }


}
