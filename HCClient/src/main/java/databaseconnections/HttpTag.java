package databaseconnections;

import actors.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicStatusLine;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpTag extends HttpCon{


    /**
     * gets all of a users tags of a user with username
     * @param username username of user
     * @param token token for authentication
     * @return a comma separated String of all the tags in the user. Returns an empty string if something goes wrong with the request
     */
    public static String getUserTags(String username,Long token){

        try {
            URIBuilder builder = new URIBuilder(URL + "/tags/"+username);
            builder.setParameter("tokenId", token.toString());
            var request = new HttpGet(builder.build());
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != 200){
                //something went wrong
                System.out.println("Something went wrong with getting the tag of "+username+" , returning an empty string");
                return "";
            }else{
                return readResponse(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }

    /**
     * Saves a tag to a logged-in user
     * @param tag name of tag to be added
     * @param tokenId authentication token of logged-in user
     * @return false if the new tag failed to get added
     */
    public static boolean saveUserTag(String tag, Long tokenId){
        try{
            URIBuilder builder = new URIBuilder(URL + "/tags");
            builder.setParameter("tag", tag);
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
     * removes a tag from a logged-in user
     * @param tag name of tag to be removed
     * @param tokenId authentication token for user to remove tag from
     * @return true if the tag was successfully removed
     */
    public static boolean removeUserTag(String tag, long tokenId){
        //TODO
        return false;
    }
}
