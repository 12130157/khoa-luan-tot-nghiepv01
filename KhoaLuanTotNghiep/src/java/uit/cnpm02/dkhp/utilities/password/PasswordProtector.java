package uit.cnpm02.dkhp.utilities.password;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author LocNguyen
 */
public class PasswordProtector {
    private static EncryptType PASSWORD_ENSCRYPT_TYPE = EncryptType.MD5;
     public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest
                    .getInstance(PASSWORD_ENSCRYPT_TYPE.value());
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
     
     public static EncryptType getEncryptType() {
        return PASSWORD_ENSCRYPT_TYPE;
    }
 
     // Unit test
    /*public static void main(String[] args) {
        try {
            System.out.println(getMD5("Javarmi.com"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/
}
