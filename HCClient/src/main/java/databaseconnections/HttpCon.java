package databaseconnections;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpCon {
    public static CloseableHttpClient client;

    private static final String URL = "http://localhost:8080";

    /**
     * inits the settings for the httpclient
     */
    public static void start(){
        try {
            client = HttpClientBuilder.create().build();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * sends a http post to the server to login
     * @param username username of user
     * @param password password of user
     * @return httpResponse from the server
     */
    public static HttpResponse login(String username, String password){
        try{

            var request = new HttpPost(URL + "/users/login");

            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("password",password));

            try {
                request.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                //do nothing
            }

            return client.execute(request);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
