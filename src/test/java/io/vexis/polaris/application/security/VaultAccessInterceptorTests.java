package io.vexis.polaris.application.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.vexis.polaris.domain.interfaces.services.VaultService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class VaultAccessInterceptorTests {

  @Mock private VaultService vaultService;

  @Test
  void shouldAllowRequestWhenVaultTokenIsValid() throws Exception {
    VaultAccessInterceptor interceptor = new VaultAccessInterceptor(vaultService);
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.addHeader("X-Vault-Token", "vault-token");
    when(vaultService.validate("vault-token")).thenReturn(true);

    boolean allowed = interceptor.preHandle(request, response, new Object());

    assertThat(allowed).isTrue();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    verify(vaultService).validate("vault-token");
  }

  @Test
  void shouldRejectRequestWhenVaultTokenIsInvalid() throws Exception {
    VaultAccessInterceptor interceptor = new VaultAccessInterceptor(vaultService);
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.addHeader("X-Vault-Token", "bad-token");
    when(vaultService.validate("bad-token")).thenReturn(false);

    boolean allowed = interceptor.preHandle(request, response, new Object());

    assertThat(allowed).isFalse();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
    verify(vaultService).validate("bad-token");
  }

  @Test
  void shouldRejectRequestWhenVaultTokenIsMissing() throws Exception {
    VaultAccessInterceptor interceptor = new VaultAccessInterceptor(vaultService);
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    when(vaultService.validate(null)).thenReturn(false);

    boolean allowed = interceptor.preHandle(request, response, new Object());

    assertThat(allowed).isFalse();
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
    verify(vaultService).validate(null);
  }
}
