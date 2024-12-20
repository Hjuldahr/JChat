import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

public class PasswodHasher 
{
    public static String HashPassword(char[] chars) throws NoSuchAlgorithmException
    {
        // Convert char[] to byte[] securely
        byte[] passwordBytes = new String(chars).getBytes(StandardCharsets.UTF_8);

        // Clear sensitive data from char[] after converting to byte[]
        Arrays.fill(chars, '\0'); // Clear char[]

        // Hash the password using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = digest.digest(passwordBytes);

        // Clear the byte[] passwordBytes
        Arrays.fill(passwordBytes, (byte) 0); // Clear byte[]

        // Convert hashed password to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedPassword) {
            hexString.append(String.format("%02x", b));
        }

        // Clear the byte[] hashedPassword
        Arrays.fill(hashedPassword, (byte) 0); // Clear byte[]

        return hexString.toString();
    }
}
