package com.spms.user.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
    private static final String SECRET = "spms_super_secret_key_for_jwt_tokens_2026";
    private static final String HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

    public static String generateToken(Long id, String email, String role) {
        String payload = String.format("{\"id\":%d,\"email\":\"%s\",\"role\":\"%s\"}", id, email, role);
        String base64Header = Base64.getUrlEncoder().withoutPadding().encodeToString(HEADER.getBytes(StandardCharsets.UTF_8));
        String base64Payload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signature = sign(base64Header + "." + base64Payload);
        return base64Header + "." + base64Payload + "." + signature;
    }

    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            String signature = sign(parts[0] + "." + parts[1]);
            return signature.equals(parts[2]);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getEmailFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return payload.split("\"email\":\"")[1].split("\"")[0];
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return payload.split("\"role\":\"")[1].split("\"")[0];
        } catch (Exception e) {
            return null;
        }
    }

    private static String sign(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error signing JWT", e);
        }
    }
}
