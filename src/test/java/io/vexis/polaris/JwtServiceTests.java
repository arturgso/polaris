package io.vexis.polaris;

import static org.assertj.core.api.Assertions.assertThat;

import io.vexis.polaris.application.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTests {

  private static final String SECRET = "test-secret-key-with-enough-length-for-hmac";

  @Test
  void shouldExtractUsernameAndValidateGeneratedToken() {
    JwtService jwtService = new JwtService(SECRET, 604800);
    UserDetails userDetails = userDetails("admin");

    String token = jwtService.generateToken(userDetails);

    assertThat(jwtService.extractUsername(token)).isEqualTo("admin");
    assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
  }

  @Test
  void shouldReturnInvalidForMalformedToken() {
    JwtService jwtService = new JwtService(SECRET, 604800);

    assertThat(jwtService.isTokenValid("not-a-jwt", userDetails("admin"))).isFalse();
  }

  @Test
  void shouldReturnInvalidForTokenSignedWithDifferentSecret() {
    JwtService jwtService = new JwtService(SECRET, 604800);
    JwtService otherJwtService = new JwtService("different-secret-key-with-enough-length", 604800);
    UserDetails userDetails = userDetails("admin");

    String token = otherJwtService.generateToken(userDetails);

    assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
  }

  @Test
  void shouldReturnInvalidForExpiredToken() {
    JwtService jwtService = new JwtService(SECRET, -1);
    UserDetails userDetails = userDetails("admin");

    String token = jwtService.generateToken(userDetails);

    assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
  }

  @Test
  void shouldGenerateAndValidateVaultTokenForAllowedUser() {
    JwtService jwtService = new JwtService(SECRET, 604800);
    ReflectionTestUtils.setField(jwtService, "initialUser", "admin");
    UserDetails userDetails = userDetails("admin");

    String token = jwtService.generateVaultToken(userDetails);

    assertThat(jwtService.isVaultTokenValid(token, "admin")).isTrue();
  }

  @Test
  void shouldRejectVaultTokenForDifferentAllowedUser() {
    JwtService jwtService = new JwtService(SECRET, 604800);
    ReflectionTestUtils.setField(jwtService, "initialUser", "admin");
    UserDetails userDetails = userDetails("admin");

    String token = jwtService.generateVaultToken(userDetails);

    assertThat(jwtService.isVaultTokenValid(token, "other-admin")).isFalse();
  }

  private UserDetails userDetails(String username) {
    return User.withUsername(username).password("password").roles("ADMIN").build();
  }
}
