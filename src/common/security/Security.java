package common.security;

import java.security.*;

public class Security {

    // General use Security class to manage an application's common.security requirements.
    // https://stackoverflow.com/questions/35685679/what-java-encryption-algorithms-should-i-use provided
    // detail for this code.

    public Security() { }

    public static String GeneratePasswordHash(String pwd) {
        /*
            Used to generate a hash for an 'open text' password. This hashed version of the
            plain text password entered by the user is then send via the network to the server
            for further processing and storage - in this case in a database.
        */

        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // create MessageDigest instance for MD5
            md.update(pwd.getBytes());  // add password bytes to digest
            byte[] bytes = md.digest(); // get the hash's bytes

            // The array bytes[] has bytes in decimal format - convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString(); // hashed password in hex format
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashedPassword;
    }
}
