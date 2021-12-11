package databaseconnections;

import actors.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

public class HttpConnection extends HttpCon{


    /**
     * gets all of a user's connections
     * @param username username of user
     * @param token token for authentication
     * @return a list of users that are friends
     */
    public static User[] getConnections(String username, Long token){
        try {
            URIBuilder builder = new URIBuilder(URL + "/connections/"+username);
            builder.setParameter("tokenId", token.toString());
            var request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                //something went wrong
                System.out.println("Something went wrong with getting the connections of "+username+" , returning null");
                return null;
            }else{
                //user jackson to build the list of users
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getEntity().getContent(), User[].class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * gets all of a user's connection requests
     * @param token token of user getting connection requests
     * @return a list of users that are requesting to connect
     */
    public static User[] getConnectionRequests(Long token){
        try {
            URIBuilder builder = new URIBuilder(URL + "/connections/requests");
            builder.setParameter("tokenId", token.toString());
            var request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                //something went wrong
                System.out.println("Something went wrong with getting the connection requests, returning null");
                return null;
            }else{
                //user jackson to build the list of users
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getEntity().getContent(), User[].class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * adds a connection request.
     * If connection request already exists from the request receiver, then that request will be accepted
     * @param username username of user that is the request receiver
     * @param tokenId token for user that is requesting
     * @return false if the request already exists
     */
    public static boolean addConnection(String username, Long tokenId){

        try{
            URIBuilder builder = new URIBuilder(URL + "/connections");
            builder.setParameter("username", username);
            builder.setParameter("tokenId", tokenId.toString());
            HttpPost request = new HttpPost(builder.build());
            HttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * removes a connection
     * @param username username of one of the connected users
     * @param tokenId token for the user of the other connected user
     * @return true if the connection was successfully removed
     */
    public static boolean removeConnection(String username, Long tokenId){
        try{
            URIBuilder builder = new URIBuilder(URL + "/connections");
            builder.setParameter("username", username);
            builder.setParameter("tokenId", tokenId.toString());
            HttpDelete request = new HttpDelete(builder.build());
            HttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
