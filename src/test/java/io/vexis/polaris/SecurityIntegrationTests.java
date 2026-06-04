package io.vexis.polaris;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vexis.polaris.domain.interfaces.repositories.UserRepository;
import io.vexis.polaris.domain.models.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldReturnUnauthorizedForPrivateRouteWithoutToken() throws Exception {
    mockMvc.perform(get("/persons")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturnUnauthorizedForSignupWithoutToken() throws Exception {
    mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"username":"new-admin","password":"secret123"}
                    """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldAllowPrivateRouteForAdmin() throws Exception {
    mockMvc.perform(get("/persons")).andExpect(status().isOk());
  }

  @Test
  void shouldKeepOpenApiPublic() throws Exception {
    mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
  }

  @Test
  void shouldLoginWithValidCredentials() throws Exception {
    createUser("admin", "secret123");

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"username":"admin","password":"secret123"}
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isString())
        .andExpect(jsonPath("$.type").value("Bearer"))
        .andExpect(jsonPath("$.expiresIn").value(604800));
  }

  @Test
  void shouldRejectInvalidCredentials() throws Exception {
    createUser("admin", "secret123");

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"username":"admin","password":"wrong-password"}
                    """))
        .andExpect(status().isUnauthorized());
  }

  private void createUser(String username, String password) {
    userRepository.save(
        User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .role("ADMIN")
            .build());
  }
}
