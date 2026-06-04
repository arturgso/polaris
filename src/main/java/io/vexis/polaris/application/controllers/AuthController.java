package io.vexis.polaris.application.controllers;

import io.vexis.polaris.application.security.JwtService;
import io.vexis.polaris.domain.interfaces.services.UserService;
import io.vexis.polaris.domain.models.dtos.users.NewUserDTO;
import io.vexis.polaris.domain.models.dtos.users.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    var userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtService.generateToken(userDetails);

    return ResponseEntity.ok(new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds()));
  }

  @PostMapping("/signup")
  public ResponseEntity<UserDTO> signup(@RequestBody @Valid NewUserDTO request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
  }

  public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

  public record LoginResponse(String token, String type, long expiresIn) {}
}
