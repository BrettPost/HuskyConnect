package actors;

import javafx.stage.Stage;
import junit.framework.TestCase;
import userinterface.GUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class UserTest extends TestCase {

    public void testGenerateImage(){
        //assertFalse(user.generateImage().);
    }


    byte[] imgBlob = null;
    public User user = null;
    public void setUp() throws Exception {
        super.setUp();

        String URL = "src\\main\\resources\\temp-logo.png";
        try {
            BufferedImage bImage = ImageIO.read(new File(URL));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage,"png",bos);
            imgBlob = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        user = new User("","","",imgBlob,"");
    }
}