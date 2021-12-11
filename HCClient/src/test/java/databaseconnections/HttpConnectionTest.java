package databaseconnections;

import actors.User;
import junit.framework.TestCase;
import org.apache.http.HttpResponse;

import java.lang.reflect.Array;
import java.util.Arrays;

public class HttpConnectionTest extends TestCase {

    Long tokenId = null;

    public void setUp() throws Exception {
        super.setUp();
        HttpCon.start();

        try {

            HttpResponse response = HttpUser.login("demo","demo");

            String str = HttpCon.readResponse(response);

            tokenId = Long.parseLong( str.strip());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void testGetConnections() {

        User[] users = HttpConnection.getConnections("demo",tokenId);
        for (User user :
                users) {
            System.out.println(user.getUsername());
        }
    }

    public void testGetConnectionRequests() {

        User[] users = HttpConnection.getConnectionRequests(tokenId);
        for (User user :
                users) {
            System.out.println(user.getUsername());
        }
    }

    public void testAddConnection(){
        System.out.println(HttpConnection.addConnection("husky",tokenId));
    }

    public void testGetUsers(){
        User[] users = HttpUser.getUsers();
        for (User user :
                users) {
            System.out.println(user.getUsername());
        }
    }
}