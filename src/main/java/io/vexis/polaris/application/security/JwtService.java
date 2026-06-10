package io.vexis.polaris.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JwtService {

  private final Algorithm algorithm;
  private final JWTVerifier verifier;
  private final long expirationSeconds;

  @Value("${app.bootstrap.admin.username}")
  private String initialUser;

  public JwtService(
      @Value("${app.jwt.secret:${JWT_SECRET:}}") String secret,
      @Value("${app.jwt.expiration-seconds:${JWT_EXPIRATION_SECONDS:604800}}")
          long expirationSeconds) {
    if (!StringUtils.hasText(secret)) {
      throw new IllegalStateException("JWT_SECRET is required");
    }
    this.algorithm = Algorithm.HMAC256(secret);
    this.verifier = JWT.require(algorithm).build();
    this.expirationSeconds = expirationSeconds;
  }

  public String generateToken(UserDetails userDetails) {
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plusSeconds(expirationSeconds);
    return JWT.create()
        .withSubject(userDetails.getUsername())
        .withIssuedAt(issuedAt)
        .withExpiresAt(expiresAt)
        .sign(algorithm);
  }

  public String generateVaultToken(UserDetails userDetails) {
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plusSeconds(900);
    return JWT.create()
        .withSubject(initialUser)
        .withClaim("scope", "vault:access")
        .withIssuedAt(issuedAt)
        .withExpiresAt(expiresAt)
        .sign(algorithm);
  }

  public String extractUsername(String token) {
    try {
      return verifier.verify(token).getSubject();
    } catch (JWTVerificationException | IllegalArgumentException exception) {
      throw new IllegalArgumentException("Invalid JWT", exception);
    }
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      return userDetails.getUsername().equals(verifier.verify(token).getSubject());
    } catch (JWTVerificationException | IllegalArgumentException exception) {
      return false;
    }
  }

  public long getExpirationSeconds() {
    return expirationSeconds;
  }
}
