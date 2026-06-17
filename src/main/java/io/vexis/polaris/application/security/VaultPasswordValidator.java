package io.vexis.polaris.application.security;

import io.vexis.polaris.domain.exceptions.VaultAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class VaultPasswordValidator {

  private final PasswordEncoder passwordEncoder;
  private final String vaultPasswordHash;

  public VaultPasswordValidator(
      PasswordEncoder passwordEncoder,
      @Value("${app.bootstrap.vault.password}") String vaultPasswordHash) {
    this.passwordEncoder = passwordEncoder;
    this.vaultPasswordHash = vaultPasswordHash;
  }

  public void validate(String password) {
    if (!StringUtils.hasText(password) || !passwordEncoder.matches(password, vaultPasswordHash)) {
      throw new VaultAuthenticationException("Incorrect Password");
    }
  }
}
