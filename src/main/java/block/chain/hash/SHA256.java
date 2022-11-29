package block.chain.hash;

import block.chain.tool.ChangeByteWIthObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    public static String encrypt(Object object) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(ChangeByteWIthObject.convertObjectToBytes(object));

        return bytesToHex(md.digest());
    }

    public static byte[] encryptByte(Object object) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(ChangeByteWIthObject.convertObjectToBytes(object));

        return md.digest();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
