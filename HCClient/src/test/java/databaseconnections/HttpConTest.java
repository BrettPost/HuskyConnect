package databaseconnections;

import junit.framework.TestCase;
import org.apache.http.HttpResponse;


public class HttpConTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
        HttpCon.start();
    }

    public void testLogin() {

        HttpResponse test = HttpCon.login("demo", "demo");
        assertEquals(test.getStatusLine().getStatusCode(), 200);
    }

    public void testSaveUser() {
    }
}