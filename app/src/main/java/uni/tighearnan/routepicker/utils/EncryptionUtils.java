package uni.tighearnan.routepicker.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tighearnan on 09/10/16.
 */

public class EncryptionUtils {
    public static String sha256(String input) {
        String output = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            output = String.format("%64x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return output;
    }
}
