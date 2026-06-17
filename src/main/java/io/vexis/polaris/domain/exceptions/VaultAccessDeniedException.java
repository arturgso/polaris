package io.vexis.polaris.domain.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class VaultAccessDeniedException extends AccessDeniedException {

  public VaultAccessDeniedException(String msg) {
    super(msg);
  }
}
