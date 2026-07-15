package io.vexis.polaris;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vexis.polaris.application.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class SecurityConfigInMemoryUserTests {

  @Test
  void shouldCreateInMemoryAdminUserFromBootstrapCredentials() {
    SecurityConfig securityConfig = new SecurityConfig();
    ReflectionTestUtils.setField(securityConfig, "bootstrapAdminUsername", "Bootstrap Admin");
    ReflectionTestUtils.setField(securityConfig, "bootstrapAdminPassword", "secret123");

    UserDetailsService userDetailsService = securityConfig.userDetailsService();
    var userDetails = userDetailsService.loadUserByUsername("bootstrap admin");

    assertTrue(securityConfig.passwordEncoder().matches("secret123", userDetails.getPassword()));
    assertTrue(
        userDetails.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
  }

  @Test
  void shouldFailWhenBootstrapAdminUsernameIsMissing() {
    SecurityConfig securityConfig = new SecurityConfig();
    ReflectionTestUtils.setField(securityConfig, "bootstrapAdminUsername", "");
    ReflectionTestUtils.setField(securityConfig, "bootstrapAdminPassword", "secret123");

    assertThrows(IllegalStateException.class, securityConfig::userDetailsService);
  }

  @Test
  void shouldFailWhenBootstrapAdminPasswordIsMissing() {
    SecurityConfig securityConfig = new SecurityConfig();
    ReflectionTestUtils.setField(securityConfig, "bootstrapAdminUsername", "admin");
    ReflectionTestUtils.setField(securityConfig, "bootstrapAdminPassword", "");

    assertThrows(IllegalStateException.class, securityConfig::userDetailsService);
  }
}
