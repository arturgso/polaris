package io.vexis.polaris.domain.exceptions;

import org.springframework.security.core.AuthenticationException;

public class VaultAuthenticationException extends AuthenticationException {

  public VaultAuthenticationException(String message) {
    super(message);
  }
}
