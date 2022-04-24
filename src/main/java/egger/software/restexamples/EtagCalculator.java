package egger.software.restexamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.EntityTag;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EtagCalculator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EntityTag calculate(Object object) throws JsonProcessingException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String value = objectMapper.writeValueAsString(object);
        byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return new EntityTag(bytesToHexString(hash));
    }

    private static String bytesToHexString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
