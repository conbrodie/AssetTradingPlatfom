package helpers;

import java.io.*;
import java.io.InputStream;
import java.util.*;

public class Helpers {
    /*
        Helpers is a class that contains static methods that can be called within the application to mainly carry
        out repetitive tasks.
    */

    public static String getProperty(Class c, String filename, String name) throws IOException {
        String value = "";

        try {
            Properties prop = new Properties();
            InputStream stream = c.getResourceAsStream("/" + filename);
            prop.load(stream);
            value = prop.getProperty(name);
        } catch (IOException e) {
            value = null;
        }
        return value;
    }
}
