package helpers.Security;

import java.security.*;

/**
 * This class assists with hashing passwords to save in the database
 * and compare when the user is logging in.
 */

public class PasswordHash {
    // Class used to Hash user passwords for security
    public PasswordHash() { }
    public static String GenPasswordHash(String password) {
        String hashedPassword = null;
        try {
                MessageDigest md = MessageDigest.getInstance("MD5"); // Create instance for MD5
                md.update(password.getBytes());
                byte[] bytes = md.digest();

                StringBuilder sb = new StringBuilder();
                for (int i =0; i < bytes.length; i++){
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }
}
