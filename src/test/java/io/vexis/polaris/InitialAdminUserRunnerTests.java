package io.vexis.polaris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vexis.polaris.application.bootstrap.InitialAdminUserRunner;
import io.vexis.polaris.domain.interfaces.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(
    properties = {
      "app.bootstrap.admin.username=Bootstrap Admin",
      "app.bootstrap.admin.password=secret123",
      "spring.datasource.url=jdbc:h2:mem:polaris-bootstrap-admin;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1"
    })
class InitialAdminUserRunnerTests {

  @Autowired private InitialAdminUserRunner initialAdminUserRunner;

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldCreateInitialAdminUserFromConfiguration() {
    var user = userRepository.findByUsername("bootstrap admin").orElseThrow();

    assertEquals("ADMIN", user.getRole());
    assertTrue(passwordEncoder.matches("secret123", user.getPassword()));
    assertTrue(
        user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
  }

  @Test
  void shouldNotDuplicateInitialAdminUserWhenRunnerExecutesAgain() throws Exception {
    initialAdminUserRunner.run(new DefaultApplicationArguments(new String[0]));

    assertEquals(1, userRepository.count());
  }
}
