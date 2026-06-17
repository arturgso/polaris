package io.vexis.polaris.application.security;

import io.vexis.polaris.domain.interfaces.services.VaultService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class VaultAccessInterceptor implements HandlerInterceptor {

  private final VaultService vaultService;

  public VaultAccessInterceptor(VaultService vaultService) {
    this.vaultService = vaultService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String token = request.getHeader("X-Vault-Token");

    if (!vaultService.validate(token)) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return false;
    }

    return true;
  }
}
