package io.vexis.polaris.application.bootstrap;

import io.vexis.polaris.domain.interfaces.repositories.UserRepository;
import io.vexis.polaris.domain.models.entities.User;
import io.vexis.polaris.shared.TextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialAdminUserRunner implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.bootstrap.admin.username:}")
  private String username;

  @Value("${app.bootstrap.admin.password:}")
  private String password;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    if (!StringUtils.hasText(username)) {
      return;
    }

    if (!StringUtils.hasText(password)) {
      throw new IllegalStateException(
          "BOOTSTRAP_ADMIN_PASSWORD is required when BOOTSTRAP_ADMIN_USERNAME is configured");
    }

    String normalizedUsername = TextUtils.normalizeText(username);

    if (userRepository.existsByUsername(normalizedUsername)) {
      log.info(
          "Initial admin user skipped because username '{}' already exists", normalizedUsername);
      return;
    }

    userRepository.saveAndFlush(
        User.builder()
            .username(normalizedUsername)
            .password(passwordEncoder.encode(password))
            .role("ADMIN")
            .build());

    log.info("Initial admin user '{}' created", normalizedUsername);
  }
}
