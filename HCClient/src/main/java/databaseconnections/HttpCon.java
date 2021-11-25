package databaseconnections;

import actors.User;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicStatusLine;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HttpCon {
    public static CloseableHttpClient client;

    private static final String URL = "http://localhost:8080";

    /**
     * inits the settings for the httpclient
     */
    public static void start() {
        try {
            client = HttpClientBuilder.create().build();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * sends a http post to the server to login
     *
     * @param username username of user
     * @param password password of user
     * @return httpResponse from the server
     */
    public static HttpResponse login(String username, String password) {
        try {

            var request = new HttpPost(URL + "/users/login");

            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));

            try {
                request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                //do nothing
            }

            return client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpResponseFactory().newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_NOT_FOUND, null), null);
        }
    }

    /**
     * gets a user
     * @param username username of user
     * @param token token for authentication
     * @return httpResponse with user as json in body
     */
    public static HttpResponse getUser(String username,Long token){

        try {
            URIBuilder builder = new URIBuilder(URL + "/users/"+username);
            builder.setParameter("tokenId", token.toString());
            var request = new HttpGet(builder.build());
            return client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpResponseFactory().newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_NOT_FOUND, null), null);
        }


    }

    /**
     * Saves a user in the database
     * @param user The user parsed from the GUI input
     * @param password The password parsed from GUI input
     * @return  true/false depending on success/failure
     */
    public static boolean saveUser(User user, String password){
        try {
            HttpPost request = new HttpPost(URL + "/users/");
            //Check if the user already exists
            if(login(user.getUsername(),password).getStatusLine().getStatusCode()==200) {
                System.out.println("Account already exists");
                return false;
            }

            //Convert parsed info into JSON
            String JSON_STRING = "{\n" +
                    "\"username\": \""+user.getUsername()+"\",\n" +
                    "\"password\": \""+password+"\",\n" +
                    "\"full_name\": \"john\",\n"+
                    "\"email\": \""+user.getEmail()+"\",\n" +
                    "\"bio\": \"john\"\n"+
                    "\"imgBlob\": \""+ Arrays.toString(user.getImgBlob()) +"\",\n" +
                    "}";

            StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            request.setEntity(requestEntity);
            System.out.println(client.execute(request));
            return true;
                
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
