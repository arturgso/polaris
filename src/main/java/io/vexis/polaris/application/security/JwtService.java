package io.vexis.polaris.application.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JwtService {

  private static final String HMAC_SHA256 = "HmacSHA256";
  private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
  private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();
  private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

  private final ObjectMapper objectMapper;
  private final byte[] secret;
  private final long expirationSeconds;

  public JwtService(
      @Value("${app.jwt.secret:${JWT_SECRET:}}") String secret,
      @Value("${app.jwt.expiration-seconds:${JWT_EXPIRATION_SECONDS:604800}}")
          long expirationSeconds) {
    if (!StringUtils.hasText(secret)) {
      throw new IllegalStateException("JWT_SECRET is required");
    }
    this.objectMapper = JsonMapper.builder().build();
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(UserDetails userDetails) {
    long issuedAt = Instant.now().getEpochSecond();
    long expiresAt = issuedAt + expirationSeconds;
    var header = Map.of("alg", "HS256", "typ", "JWT");
    var payload = Map.of("sub", userDetails.getUsername(), "iat", issuedAt, "exp", expiresAt);

    String headerPart = encodeJson(header);
    String payloadPart = encodeJson(payload);
    String unsignedToken = headerPart + "." + payloadPart;

    return unsignedToken + "." + sign(unsignedToken);
  }

  public String extractUsername(String token) {
    return readPayload(token).get("sub").toString();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      return isSignatureValid(token)
          && userDetails.getUsername().equals(extractUsername(token))
          && !isExpired(token);
    } catch (IllegalArgumentException exception) {
      return false;
    }
  }

  public long getExpirationSeconds() {
    return expirationSeconds;
  }

  private boolean isExpired(String token) {
    var exp = readPayload(token).get("exp");
    long expiresAt =
        exp instanceof Number number ? number.longValue() : Long.parseLong(exp.toString());
    return Instant.now().getEpochSecond() >= expiresAt;
  }

  private boolean isSignatureValid(String token) {
    String[] parts = split(token);
    String unsignedToken = parts[0] + "." + parts[1];
    return sign(unsignedToken).equals(parts[2]);
  }

  private Map<String, Object> readPayload(String token) {
    String[] parts = split(token);
    try {
      return objectMapper.readValue(BASE64_URL_DECODER.decode(parts[1]), MAP_TYPE);
    } catch (IOException exception) {
      throw new IllegalArgumentException("Invalid JWT payload", exception);
    }
  }

  private String[] split(String token) {
    String[] parts = token.split("\\.");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid JWT format");
    }
    return parts;
  }

  private String encodeJson(Map<String, ?> value) {
    try {
      return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException("Unable to write JWT JSON", exception);
    }
  }

  private String sign(String value) {
    try {
      Mac mac = Mac.getInstance(HMAC_SHA256);
      mac.init(new SecretKeySpec(secret, HMAC_SHA256));
      return BASE64_URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
      throw new IllegalStateException("Unable to sign JWT", exception);
    }
  }
}
