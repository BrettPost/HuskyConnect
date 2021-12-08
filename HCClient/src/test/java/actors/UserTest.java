package actors;

import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class UserTest extends TestCase {

    public void testGenerateImage(){
        assertEquals(imgBlob[0] + imgBlob[1],user.getImg_blob()[0] + user.getImg_blob()[1]);
    }


    byte[] imgBlob = null;
    public User user = null;
    public void setUp() throws Exception {
        super.setUp();

        String URL = "file:/C:/Users/josep/Documents/GitHub/HuskyConnect/HCClient/src/main/resources/husky-connect-user-img.jpg";
        try {
            BufferedImage bImage = ImageIO.read(new File(new URI(URL)));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage,"jpg",bos);
            imgBlob = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        user = new User("","","",URL,"");
    }
}