package databaseconnections;

import junit.framework.TestCase;
import org.apache.http.HttpResponse;


public class HttpConTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
        HttpCon.start();
    }

    public void testLogin() {

        HttpResponse test1 = HttpCon.login("demo", "demo");
        assertEquals(test1.getStatusLine().getStatusCode(), 200);   //correct username and password
        HttpResponse test2 = HttpCon.login("demo", "demo1");
        assertEquals(test2.getStatusLine().getStatusCode(), 401);   //incorrect username or password
    }

    public void testSaveUser() {

    }
}