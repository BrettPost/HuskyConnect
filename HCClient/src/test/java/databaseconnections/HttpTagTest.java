package databaseconnections;

import junit.framework.TestCase;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpTagTest extends TestCase {

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

    public void testGetUserTags() {
        try{
            String tags = HttpTag.getUserTags("demo", tokenId);
            assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

    }

    public void testSaveUserTag() {
        try{
            boolean result = HttpTag.saveUserTag("swimmer", tokenId);
            assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    public void testRemoveUserTag() {
        fail();
    }
}