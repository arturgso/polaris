package io.vexis.polaris;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldReturnUnauthorizedForPrivateRouteWithoutToken() throws Exception {
    mockMvc.perform(get("/persons")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldAllowCorsPreflightFromLocalFrontend() throws Exception {
    mockMvc
        .perform(
            options("/persons")
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "authorization,content-type"))
        .andExpect(status().isOk())
        .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
        .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
  }

  @Test
  void shouldIncludeCorsHeadersOnUnauthorizedResponseFromLocalFrontend() throws Exception {
    mockMvc
        .perform(get("/persons").header("Origin", "http://localhost:5173"))
        .andExpect(status().isUnauthorized())
        .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
        .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
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

}
