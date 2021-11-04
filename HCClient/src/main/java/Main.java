import databaseconnections.HttpCon;
import userinterface.GUI;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException, InterruptedException {

        HttpCon.start();
        GUI.start();

    }

}
