package io.vexis.polaris.application.controllers;

import io.vexis.polaris.application.security.JwtService;
import io.vexis.polaris.shared.TextUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Value("${app.bootstrap.admin.username:}")
  private String bootstrapAdminUsername;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    String normalizedBootstrapUsername = TextUtils.normalizeText(bootstrapAdminUsername);
    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                normalizedBootstrapUsername, request.password()));
    var userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtService.generateToken(userDetails);

    return ResponseEntity.ok(new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds()));
  }

  public record LoginRequest(@NotBlank String password) {}

  public record LoginResponse(String token, String type, long expiresIn) {}
}
